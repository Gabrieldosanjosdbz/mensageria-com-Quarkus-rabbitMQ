package br.com.alura.service.saga;

import br.com.alura.domain.Agencia;
import br.com.alura.repository.saga.SagaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.vertx.core.Vertx;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;

import java.time.LocalDateTime;

@ApplicationScoped
public class SagaResyncService {

    private final MutinyEmitter<br.com.alura.avro.Agencia> kafkaEmmiter;

    private final SagaRepository sagaRepository;

    private final ObjectMapper  objectMapper;

    private final Vertx vertx;

    public SagaResyncService(
            @Channel("remover-agencia-channel") MutinyEmitter<br.com.alura.avro.Agencia> kafkaEmmiter,
            SagaRepository sagaRepository,
            Vertx vertx) {
        this.kafkaEmmiter = kafkaEmmiter;
        this.sagaRepository = sagaRepository;
        this.objectMapper = new ObjectMapper();
        this.vertx = vertx;
    }

    //Este worker rodara de 10 em 10s
    @Scheduled(every = "10s", skipExecutionIf = Scheduled.ApplicationNotRunning.class)
    public void resync() {
        // iniciando contexto reativo event loop
        vertx.runOnContext(v -> {
            LocalDateTime limite = LocalDateTime.now().minusMinutes(2);

            // abre uma sessão do Hibernate Reactive (a Mutiny.Session), roda o que você passou dentro dela e fecha a sessão no final.
            Panache.withSession(() -> sagaRepository.listByStatusAndCreatedAt(limite))
                    .subscribe().with(sagas -> {
                        sagas.forEach(saga -> {
                            try {
                                Agencia agencia = objectMapper.readValue(saga.getEntidade(), Agencia.class);
                                kafkaEmmiter.sendAndForget(new br.com.alura.avro.Agencia(agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getSituacaoCadastral()));
                            } catch (Exception e) {
                                Log.errorf(e, "Erro ao reenviar saga %s", saga.getId());
                            }
                        });
                    }, falha -> Log.error("Erro ao buscar sagas abertas", falha));
        });
    }
}
