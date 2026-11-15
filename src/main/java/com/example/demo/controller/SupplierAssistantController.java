package com.example.demo.controller;

import com.example.demo.service.SupplierAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplier-assistant")
public class SupplierAssistantController {

    private final SupplierAssistantService supplierAssistantService;

    public SupplierAssistantController(SupplierAssistantService supplierAssistantService) {
        this.supplierAssistantService = supplierAssistantService;
    }

    @PostMapping
    public ResponseEntity<SupplierAssistantService.AssistantReply> ask(@RequestBody AssistantQuestion request) {
        if (request.question() == null || request.question().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(supplierAssistantService.answer(request.question().trim()));
    }

    public record AssistantQuestion(String question) {
    }
}
