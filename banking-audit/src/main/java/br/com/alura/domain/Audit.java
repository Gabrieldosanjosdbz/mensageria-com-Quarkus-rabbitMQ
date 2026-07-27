package br.com.alura.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity

public class Audit {

    public Audit() {

    }

    public Audit(Long id, String cnpj, String status) {
        this.id = id;
        this.cnpj = cnpj;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpj;

    private String status;

    public Long getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getStatus() {
        return status;
    }
}
