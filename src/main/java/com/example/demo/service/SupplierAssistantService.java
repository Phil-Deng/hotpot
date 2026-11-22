package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Supplier;
import com.example.demo.repository.ProductRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SupplierAssistantService {

    private static final Locale TAIWAN = Locale.TAIWAN;
    private static final ZoneId TAIPEI_ZONE = ZoneId.of("Asia/Taipei");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy/MM/dd（EEEE）HH:mm", TAIWAN);

    private final ProductRepository productRepository;
    private final SupplierScheduleService supplierScheduleService;
    private final ObjectProvider<ChatClient.Builder> chatClientBuilder;
    private final boolean aiEnabled;

    public SupplierAssistantService(ProductRepository productRepository,
                                    SupplierScheduleService supplierScheduleService,
                                    ObjectProvider<ChatClient.Builder> chatClientBuilder,
                                    @Value("${app.ai.enabled:false}") boolean aiEnabled) {
        this.productRepository = productRepository;
        this.supplierScheduleService = supplierScheduleService;
        this.chatClientBuilder = chatClientBuilder;
        this.aiEnabled = aiEnabled;
    }

    public AssistantReply answer(String question) {
        LocalDateTime now = LocalDateTime.now(TAIPEI_ZONE);
        Optional<Product> product = productRepository.findAll().stream()
                .map(item -> new ScoredProduct(item, longestCommonSubstring(item.getName(), question)))
                .filter(item -> item.score() >= 2)
                .max(Comparator.comparingInt(ScoredProduct::score))
                .map(ScoredProduct::product);
        Supplier supplier = product.map(item -> supplierScheduleService.findByName(item.getSupplier()))
                .orElseGet(() -> supplierScheduleService.findAll().stream()
                        .filter(item -> question.contains(item.getName()))
                        .max(Comparator.comparingInt(item -> item.getName().length()))
                        .orElse(null));

        if (supplier == null) {
            return new AssistantReply("我可以查詢目前已建檔品項或廠商的叫貨截止時間、休息日與預計到貨日。請告訴我品項名稱或廠商名稱，例如：「番茄今天叫貨何時到？」", false);
        }

        String factualAnswer = factualAnswer(supplier, product.orElse(null), now);
        String aiAnswer = improveWithAi(question, factualAnswer);
        return new AssistantReply(aiAnswer == null ? factualAnswer : aiAnswer, aiAnswer != null);
    }

    private String factualAnswer(Supplier supplier, Product product, LocalDateTime orderedAt) {
        SupplierScheduleService.DeliveryEstimate estimate = supplierScheduleService.estimate(supplier, orderedAt);
        String subject = product == null ? supplier.getName() : "「" + product.getName() + "」由 " + supplier.getName() + " 供應";
        String restDays = supplierScheduleService.restDays(supplier).stream()
                .sorted()
                .map(day -> "週" + day.getDisplayName(TextStyle.NARROW, TAIWAN))
                .reduce((left, right) -> left + "、" + right)
                .orElse("無");
        String handling = estimate.deferred()
                ? "此刻下單會視為隔日 15:00 前的訂單"
                : "此刻下單仍在今日 15:00 前的叫貨時段";
        return subject + "。\n"
                + "• 叫貨截止：每天下午 " + supplier.getOrderCutoffTime() + " 前。\n"
                + "• 休息日：" + restDays + "。\n"
                + "• " + DATE_TIME.format(orderedAt) + " 下單時，" + handling + "（訂單處理日："
                + estimate.effectiveOrderDate().format(DateTimeFormatter.ofPattern("M/d（EEEE）", TAIWAN)) + "）。\n"
                + "• 預計到貨：" + estimate.deliveryDate().format(DateTimeFormatter.ofPattern("M/d（EEEE）", TAIWAN))
                + "；若配送日遇供應商休息日，系統已自動順延。";
    }

    private String improveWithAi(String question, String factualAnswer) {
        ChatClient.Builder builder = aiEnabled ? chatClientBuilder.getIfAvailable() : null;
        if (builder == null) {
            return null;
        }
        try {
            return builder.build().prompt()
                    .system("你是火鍋店採購助理。只能根據提供的供應商規則回答，不可編造規則或日期；以繁體中文，簡潔條列。")
                    .user("使用者問題：" + question + "\n\n已驗證的規則與計算結果：\n" + factualAnswer
                            + "\n\n請將上述內容整理為直接回答。")
                    .call()
                    .content();
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public record AssistantReply(String answer, boolean generatedByAi) {
    }

    private int longestCommonSubstring(String first, String second) {
        int best = 0;
        int[][] lengths = new int[first.length() + 1][second.length() + 1];
        for (int firstIndex = 1; firstIndex <= first.length(); firstIndex++) {
            for (int secondIndex = 1; secondIndex <= second.length(); secondIndex++) {
                if (first.charAt(firstIndex - 1) == second.charAt(secondIndex - 1)) {
                    lengths[firstIndex][secondIndex] = lengths[firstIndex - 1][secondIndex - 1] + 1;
                    best = Math.max(best, lengths[firstIndex][secondIndex]);
                }
            }
        }
        return best;
    }

    private record ScoredProduct(Product product, int score) {
    }
}
