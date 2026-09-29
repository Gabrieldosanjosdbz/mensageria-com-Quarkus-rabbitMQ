package com.alura.agencias.service;

import com.alura.agencias.repository.AgenciaRepository;
import com.alura.agencias.service.http.saga.SagaHttpService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class RemoverAgenciaService {

    @RestClient
    SagaHttpService sagaHttpService;

    private final AgenciaRepository agenciaRepository;

    public RemoverAgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    @WithTransaction
    @Incoming("remover-agencia-channel")
    public Uni<Void> consumirMensagem(com.alura.agencias.domain.Agencia mensagem) {
        Log.infof("Removendo agencia com cnpj %s", mensagem.getCnpj());
        return agenciaRepository.findByCnpj(mensagem.getCnpj())
                .onItem().ifNotNull().transformToUni(agencia ->
                        agenciaRepository.deleteById(agencia.getId())
                                .call(() -> sagaHttpService.fecharSaga(agencia.getCnpj())) // fazendo requisição para fechar saga
                ).replaceWithVoid();
    }
}
