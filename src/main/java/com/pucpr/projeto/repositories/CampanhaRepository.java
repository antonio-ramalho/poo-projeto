package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Campanha;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;

import java.util.List;

public class CampanhaRepository extends AbstractArquivo<Campanha> implements ICrud<Campanha, Long> {

    public CampanhaRepository() {
        super("data/campanha.dat");
    }

    @Override
    public void salvar(Campanha entidade) {
        List<Campanha> campanhas = buscarTodosArquivo();
        campanhas.add(entidade);
        salvarTodosArquivo(campanhas);
    }

    @Override
    public Campanha buscarPorId(Long id) {
        return buscarTodosArquivo().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DomainException("Campanha não encontrada."));
    }

    @Override
    public List<Campanha> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Campanha entidade) {
        List<Campanha> campanhas = buscarTodosArquivo();
        for (int i = 0; i < campanhas.size(); i++) {
            if (campanhas.get(i).getId().equals(entidade.getId())) {
                campanhas.set(i, entidade);
                salvarTodosArquivo(campanhas);
                return;
            }
        }
        throw new DomainException("Não foi possível concluir a atualização! Campanha não encontrada.");
    }

    @Override
    public void excluir(Long id) {
        List<Campanha> campanhas = buscarTodosArquivo();
        boolean removido = campanhas.removeIf(c -> c.getId().equals(id));
        if (!removido) {
            throw new DomainException("Falha na exclusão, Campanha não encontrada!");
        }
        salvarTodosArquivo(campanhas);
    }
}