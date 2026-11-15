package com.example.demo.controller;

import com.example.demo.model.Supplier;
import com.example.demo.service.SupplierScheduleService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierScheduleService supplierScheduleService;

    public SupplierController(SupplierScheduleService supplierScheduleService) {
        this.supplierScheduleService = supplierScheduleService;
    }

    @GetMapping
    public List<Supplier> getSuppliers() {
        return supplierScheduleService.findAll();
    }
}
