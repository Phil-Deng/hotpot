package com.example.demo;

import com.example.demo.model.SharedState;
import com.example.demo.repository.SharedStateRepository;
import com.example.demo.service.SharedStateService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SharedStateServiceTest {
    @Test
    void preservesLegacyHistoryOnlyForXinyiAndKeepsStoreWritesSeparate() {
        var repository = mock(SharedStateRepository.class);
        Map<String, SharedState> records = new HashMap<>();
        records.put("orders", new SharedState("orders", "[{\"id\":1}]"));
        records.put("stocktakes", new SharedState("stocktakes", "{\"2026-11\":{}}"));
        when(repository.findById(anyString())).thenAnswer(call -> Optional.ofNullable(records.get(call.getArgument(0))));
        when(repository.save(any(SharedState.class))).thenAnswer(call -> {
            SharedState value = call.getArgument(0);
            records.put(value.getStateKey(), value);
            return value;
        });
        var service = new SharedStateService(repository);
        assertEquals("[{\"id\":1}]", service.get("orders-xinyi"));
        assertNull(service.get("orders-daan"));
        assertEquals("{\"2026-11\":{}}", service.get("stocktakes-xinyi"));
        assertNull(service.get("stocktakes-daan"));
        service.save("orders-daan", "[{\"id\":2}]");
        assertEquals("[{\"id\":1}]", service.get("orders-xinyi"));
        assertEquals("[{\"id\":2}]", service.get("orders-daan"));
        service.save("orders-xinyi", "[]");
        assertEquals("[]", service.get("orders-xinyi"));
        assertEquals("[{\"id\":1}]", service.get("orders"));
    }
}
