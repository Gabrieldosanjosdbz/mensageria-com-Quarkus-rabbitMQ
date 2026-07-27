package br.com.alura.repository;

import br.com.alura.domain.Audit;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class BankingAuditRepository implements PanacheRepository<Audit> {
}
