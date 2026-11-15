package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.model.Supplier;
import com.example.demo.service.SupplierScheduleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SupplierScheduleServiceTest {

    private final SupplierScheduleService service = new SupplierScheduleService(null);

    @Test
    void shiftsDeliveryPastSupplierRestDays() {
        Supplier supplier = supplier("TUESDAY,SUNDAY");

        var estimate = service.estimate(supplier, LocalDateTime.of(2026, 11, 16, 14, 0)); // Monday

        assertEquals(LocalDate.of(2026, 11, 16), estimate.effectiveOrderDate());
        assertEquals(LocalDate.of(2026, 11, 18), estimate.deliveryDate()); // Tuesday is a rest day
    }

    @Test
    void treatsLateOrderAsNextDayOrder() {
        Supplier supplier = supplier("MONDAY");

        var estimate = service.estimate(supplier, LocalDateTime.of(2026, 11, 10, 15, 0)); // Tuesday 15:00

        assertEquals(LocalDate.of(2026, 11, 11), estimate.effectiveOrderDate());
        assertEquals(LocalDate.of(2026, 11, 12), estimate.deliveryDate());
    }

    private Supplier supplier(String restDays) {
        Supplier supplier = new Supplier();
        supplier.setRestDays(restDays);
        supplier.setOrderCutoffTime("15:00");
        return supplier;
    }
}
