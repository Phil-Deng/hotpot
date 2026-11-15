package com.example.demo.service;

import com.example.demo.model.SharedState;
import com.example.demo.repository.SharedStateRepository;
import org.springframework.stereotype.Service;

@Service
public class SharedStateService {

    private final SharedStateRepository repository;
    public SharedStateService(SharedStateRepository repository) {
        this.repository = repository;
    }

    public String get(String key) {
        return repository.findById(key)
                .map(SharedState::getPayload)
                .orElse(null);
    }

    public void save(String key, String payload) {
        repository.save(new SharedState(key, payload));
    }
}
