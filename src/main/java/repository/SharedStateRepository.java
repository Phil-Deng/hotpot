package com.example.demo.repository;

import com.example.demo.model.SharedState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedStateRepository extends JpaRepository<SharedState, String> {
}
