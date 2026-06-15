package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Depoimento;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class DepoimentoRepository extends AbstractArquivo<Depoimento> implements ICrud<Depoimento, String> {

    public DepoimentoRepository() {
        super("data/depoimento.dat");
    }

    @Override
    public void salvar(Depoimento entidade) {
        List<Depoimento> depoimento = buscarTodosArquivo();
        depoimento.add(entidade);
        salvarTodosArquivo(depoimento);
    }

    @Override
    public Depoimento buscarPorId(String id) {
        List<Depoimento> depoimentos = buscarTodosArquivo();

        Depoimento depoimento = depoimentos.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

        if (depoimento == null) {
            throw new DomainException("Depoimento não encontrado");
        }
        return depoimento;
    }

    @Override
    public List<Depoimento> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Depoimento entidade) {
        List<Depoimento> depoimentos = buscarTodosArquivo();

        for (int i = 0; i < depoimentos.size(); i++) {

            if (depoimentos.get(i).getId().equals(entidade.getId())) {
                depoimentos.set(i, entidade);
                salvarTodosArquivo(depoimentos);
                return;
            }
        }

        throw new DomainException("Não foi possível concluir a atualização! Depoimento não encontrado.");
    }

    @Override
    public void excluir(String id) {
        List<Depoimento> depoimentos = buscarTodosArquivo();

        boolean excluir = depoimentos.removeIf(u -> u.getId().equals(id));

        if (!excluir) {
            throw new DomainException("Falha na exclusão, depoimento não encontrado!");
        }
        salvarTodosArquivo(depoimentos);
    }
}
