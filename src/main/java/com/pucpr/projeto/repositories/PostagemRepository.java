package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Postagem;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;

import java.util.List;

public class PostagemRepository extends AbstractArquivo<Postagem> implements ICrud<Postagem, Long> {

    public PostagemRepository() {
        super("data/postagem.dat");
    }

    @Override
    public void salvar(Postagem entidade) {
        List<Postagem> postagens = buscarTodosArquivo();
        postagens.add(entidade);
        salvarTodosArquivo(postagens);
    }

    @Override
    public Postagem buscarPorId(Long id) {
        return buscarTodosArquivo().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DomainException("Postagem não encontrada."));
    }

    @Override
    public List<Postagem> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Postagem entidade) {
        List<Postagem> postagens = buscarTodosArquivo();
        for (int i = 0; i < postagens.size(); i++) {
            if (postagens.get(i).getId().equals(entidade.getId())) {
                postagens.set(i, entidade);
                salvarTodosArquivo(postagens);
                return;
            }
        }
        throw new DomainException("Não foi possível concluir a atualização! Postagem não encontrada.");
    }

    @Override
    public void excluir(Long id) {
        List<Postagem> postagens = buscarTodosArquivo();
        boolean removido = postagens.removeIf(p -> p.getId().equals(id));
        if (!removido) {
            throw new DomainException("Falha na exclusão, Postagem não encontrada!");
        }
        salvarTodosArquivo(postagens);
    }
}