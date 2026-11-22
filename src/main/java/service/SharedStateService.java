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
        String payload = repository.findById(key).map(SharedState::getPayload).orElse(null);
        // Existing shared records belong to the original Xinyi store. Keep them intact.
        if (payload == null && (key.equals("orders-xinyi") || key.equals("stocktakes-xinyi"))) {
            return repository.findById(key.substring(0, key.lastIndexOf('-')))
                    .map(SharedState::getPayload).orElse(null);
        }
        return payload;
    }

    public void save(String key, String payload) {
        repository.save(new SharedState(key, payload));
    }
}
