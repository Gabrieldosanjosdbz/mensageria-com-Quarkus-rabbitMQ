package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.audit.Audit;
import br.com.alura.repository.SituacaoCadastralRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class SituacaoCadastralService {

    private final SituacaoCadastralRepository situacaoCadastralRepository;

    private final Emitter<Audit> emitter;

    // nome qualificado porque br.com.alura.domain.Agencia já ocupa o nome simples
    private final MutinyEmitter<br.com.alura.avro.Agencia> mutinyEmitter;

    public SituacaoCadastralService(SituacaoCadastralRepository situacaoCadastralRepository,
                                    @Channel("notificacoes") Emitter<Audit> emmiter,
                                    @Channel("remover-agencia-channel") MutinyEmitter<br.com.alura.avro.Agencia> mutinyEmitter
    ) {
        this.situacaoCadastralRepository = situacaoCadastralRepository;
        this.emitter = emmiter;
        this.mutinyEmitter = mutinyEmitter;
    }

    @WithTransaction
    public Uni<Void> alterar(Agencia agencia) {
        return situacaoCadastralRepository
                .update("situacaoCadastral = ?1 where cnpj = ?2",
                        agencia.getSituacaoCadastral(), agencia.getCnpj())
                .onItem().invoke(() -> {
                    emitter.send(new Audit(agencia.getId(), agencia.getCnpj(), agencia.getSituacaoCadastral()));
                })
                .call(() -> {
                    if (agencia.getSituacaoCadastral().equals("INATIVO")) {
                        return mutinyEmitter.send(paraAvro(agencia));
                    }
                    return Uni.createFrom().voidItem();
                })
                .replaceWithVoid();
    }

    private br.com.alura.avro.Agencia paraAvro(Agencia agencia) {
        return br.com.alura.avro.Agencia.newBuilder()
                .setNome(agencia.getNome())
                .setRazaoSocial(agencia.getRazaoSocial())
                .setCnpj(agencia.getCnpj())
                .setSituacaoCadastral(agencia.getSituacaoCadastral())
                .build();
    }
}
