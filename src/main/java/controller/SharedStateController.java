package com.example.demo.controller;

import com.example.demo.service.SharedStateService;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/shared-state")
public class SharedStateController {

    private static final Set<String> ALLOWED_KEYS = Set.of("orders", "stocktakes");

    private final SharedStateService sharedStateService;

    public SharedStateController(SharedStateService sharedStateService) {
        this.sharedStateService = sharedStateService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        validateKey(key);
        String data = sharedStateService.get(key);
        String response = data == null ? "{\"exists\":false,\"data\":{}}" : "{\"exists\":true,\"data\":" + data + "}";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PutMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable String key, @RequestBody String data) {
        validateKey(key);
        if (data == null || data.isBlank() || !(data.trim().startsWith("[") || data.trim().startsWith("{"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shared state must be JSON data");
        }
        sharedStateService.save(key, data);
    }

    private void validateKey(String key) {
        if (!ALLOWED_KEYS.contains(key)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shared state not found");
        }
    }
}
