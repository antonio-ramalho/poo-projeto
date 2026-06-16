package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Despesa;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class DespesaRepository extends AbstractArquivo<Despesa> implements ICrud<Despesa, Long> {

    public DespesaRepository() {
        super("data/despesa.dat");
    }

    @Override
    public void salvar(Despesa entidade) {
        List<Despesa> despesas = buscarTodosArquivo();
        despesas.add(entidade);
        salvarTodosArquivo(despesas);
    }

    @Override
    public Despesa buscarPorId(Long id) {
        return buscarTodosArquivo().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DomainException("Despesa não encontrada"));
    }

    @Override
    public List<Despesa> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Despesa entidade) {
        List<Despesa> despesas = buscarTodosArquivo();
        for (int i = 0; i < despesas.size(); i++) {
            if (despesas.get(i).getId().equals(entidade.getId())) {
                despesas.set(i, entidade);
                salvarTodosArquivo(despesas);
                return;
            }
        }
        throw new DomainException("Não foi possível atualizar! Despesa não encontrada.");
    }

    @Override
    public void excluir(Long id) {
        List<Despesa> despesas = buscarTodosArquivo();
        boolean excluido = despesas.removeIf(d -> d.getId().equals(id));
        if (!excluido) throw new DomainException("Falha na exclusão: despesa não encontrada!");
        salvarTodosArquivo(despesas);
    }
}