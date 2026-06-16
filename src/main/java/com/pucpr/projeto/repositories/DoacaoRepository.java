package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Doacao;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class DoacaoRepository extends AbstractArquivo<Doacao> implements ICrud<Doacao, Long> {

    public DoacaoRepository() {
        super("data/doacao.dat");
    }

    @Override
    public void salvar(Doacao entidade) {
        List<Doacao> doacoes = buscarTodosArquivo();
        doacoes.add(entidade);
        salvarTodosArquivo(doacoes);
    }

    @Override
    public Doacao buscarPorId(Long id) {
        List<Doacao> doacoes = buscarTodosArquivo();
        Doacao doacao = doacoes.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);

        if (doacao == null) {
            throw new DomainException("Doação não encontrada");
        }
        return doacao;
    }

    @Override
    public List<Doacao> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Doacao entidade) {
        List<Doacao> doacoes = buscarTodosArquivo();
        for (int i = 0; i < doacoes.size(); i++) {
            if (doacoes.get(i).getId().equals(entidade.getId())) {
                doacoes.set(i, entidade);
                salvarTodosArquivo(doacoes);
                return;
            }
        }
        throw new DomainException("Não foi possível concluir a atualização! Doação não encontrada.");
    }

    @Override
    public void excluir(Long id) {
        List<Doacao> doacoes = buscarTodosArquivo();
        boolean excluido = doacoes.removeIf(d -> d.getId().equals(id));

        if (!excluido) {
            throw new DomainException("Falha na exclusão: doação não encontrada!");
        }
        salvarTodosArquivo(doacoes);
    }
}