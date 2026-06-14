package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class PessoaFisicaRepository extends AbstractArquivo<PessoaFisica> implements ICrud<PessoaFisica, String> {

    public PessoaFisicaRepository() {
        super("data/pessoaFisica.dat");
    }

    @Override
    public void salvar(PessoaFisica entidade) {
        List<PessoaFisica> pessoasFisica = buscarTodosArquivo();
        pessoasFisica.add(entidade);
        salvarTodosArquivo(pessoasFisica);
    }

    @Override
    public PessoaFisica buscarPorId(String id) {
        List<PessoaFisica> pessoas = buscarTodosArquivo();

        PessoaFisica pessoa = pessoas.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

        if (pessoa == null) {
            throw new DomainException("Usuário não encontrado");
        }
        return pessoa;
    }

    @Override
    public List<PessoaFisica> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(PessoaFisica entidade) {
        List<PessoaFisica> pessoas = buscarTodosArquivo();

        for (int i = 0; i < pessoas.size(); i++) {

            if (pessoas.get(i).getId().equals(entidade.getId())) {
                pessoas.set(i, entidade);
                salvarTodosArquivo(pessoas);
                return;
            }
        }

        throw new DomainException("Não foi possível concluir a atualização! Usuário não encontrado.");
    }

    @Override
    public void excluir(String id) {
        List<PessoaFisica> pessoasFisicas = buscarTodosArquivo();

        boolean excluir = pessoasFisicas.removeIf(u -> u.getId().equals(id));

        if (!excluir) {
            throw new DomainException("Falha na exclusão, usuário não encontrado!");
        }
        salvarTodosArquivo(pessoasFisicas);
    }
}
