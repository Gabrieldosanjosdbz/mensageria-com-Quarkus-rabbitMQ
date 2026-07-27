package br.com.alura.controller;

import br.com.alura.domain.Audit;
import br.com.alura.repository.BankingAuditRepository;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/audits")
public class BankingAuditController {

    private final BankingAuditRepository repository;

    BankingAuditController(BankingAuditRepository repository) {
        this.repository = repository;
    }

    @POST
    public Uni<RestResponse<Void>> cadastrar(Audit audit) {
        return this.repository.persist(audit)
                .replaceWith(RestResponse.ok());
    }
}
