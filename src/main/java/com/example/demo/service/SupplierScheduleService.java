package com.example.demo.service;

import com.example.demo.model.Supplier;
import com.example.demo.repository.SupplierRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SupplierScheduleService {

    private final SupplierRepository supplierRepository;

    public SupplierScheduleService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier findByName(String name) {
        return supplierRepository.findByName(name).orElse(null);
    }

    public DeliveryEstimate estimate(Supplier supplier, LocalDateTime orderedAt) {
        Set<DayOfWeek> restDays = restDays(supplier);
        LocalTime cutoff = LocalTime.parse(supplier.getOrderCutoffTime());
        LocalDate effectiveOrderDate = orderedAt.toLocalDate();
        boolean deferred = restDays.contains(effectiveOrderDate.getDayOfWeek())
                || !orderedAt.toLocalTime().isBefore(cutoff);
        if (deferred) {
            effectiveOrderDate = effectiveOrderDate.plusDays(1);
        }

        LocalDate deliveryDate = effectiveOrderDate.plusDays(1);
        while (restDays.contains(deliveryDate.getDayOfWeek())) {
            deliveryDate = deliveryDate.plusDays(1);
        }
        return new DeliveryEstimate(effectiveOrderDate, deliveryDate, deferred);
    }

    public Set<DayOfWeek> restDays(Supplier supplier) {
        return Arrays.stream(supplier.getRestDays().split(","))
                .filter(value -> !value.isBlank())
                .map(value -> DayOfWeek.valueOf(value.trim().toUpperCase(Locale.ROOT)))
                .collect(() -> EnumSet.noneOf(DayOfWeek.class), Set::add, Set::addAll);
    }

    public record DeliveryEstimate(LocalDate effectiveOrderDate, LocalDate deliveryDate, boolean deferred) {
    }
}
