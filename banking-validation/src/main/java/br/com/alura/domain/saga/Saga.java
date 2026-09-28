package br.com.alura.domain.saga;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Saga {

    public Saga(String id, String entidade, SagaStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.entidade = entidade;
        this.status = status;
        this.createdAt = createdAt;
    }

    protected Saga() {
        id = null;
        entidade = null;
        status = null;
        createdAt = null;
    }

    @Id
    private final String id;

    private final String entidade;

    @Enumerated(EnumType.STRING)
    private final SagaStatus status;

    @Column(name = "created_at")
    private final LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public String getEntidade() {
        return entidade;
    }

    public SagaStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
