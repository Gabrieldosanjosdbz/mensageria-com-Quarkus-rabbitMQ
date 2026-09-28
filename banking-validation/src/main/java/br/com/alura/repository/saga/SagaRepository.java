package br.com.alura.repository.saga;

import br.com.alura.domain.saga.Saga;
import br.com.alura.domain.saga.SagaStatus;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SagaRepository implements PanacheRepository<Saga> {
    public Uni<List<Saga>> listByStatusAndCreatedAt(LocalDateTime limite) {
        return list("createdAt < ?1 and status = ?2", limite, SagaStatus.OPEN);
    }
}
