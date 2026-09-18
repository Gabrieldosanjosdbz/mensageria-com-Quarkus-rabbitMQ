package com.alura.agencias.service;

import br.com.alura.avro.Agencia;
import com.alura.agencias.repository.AgenciaRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class RemoverAgenciaService {

    private final AgenciaRepository agenciaRepository;

    public RemoverAgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    @WithTransaction
    @Incoming("remover-agencia-channel")
    public Uni<Void> consumirMensagem(Agencia mensagem) {
        Log.infof("Removendo agencia com cnpj %s", mensagem.getCnpj());
        return agenciaRepository.findByCnpj(mensagem.getCnpj())
                .onItem().ifNotNull().transformToUni(agencia ->
                        agenciaRepository.deleteById(agencia.getId())
                ).replaceWithVoid();
    }
}
