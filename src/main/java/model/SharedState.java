package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shared_state")
@Getter
@Setter
@NoArgsConstructor
public class SharedState {

    @Id
    @Column(length = 64)
    private String stateKey;

    @Lob
    @Column(nullable = false)
    private String payload;

    public SharedState(String stateKey, String payload) {
        this.stateKey = stateKey;
        this.payload = payload;
    }
}
