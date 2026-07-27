package br.com.alura;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SituacaoCadastralRepository implements PanacheRepository<Agencia> {

    public Agencia findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }
}
