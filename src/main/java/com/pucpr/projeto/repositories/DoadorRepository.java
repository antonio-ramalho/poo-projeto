package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Doador;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class DoadorRepository extends AbstractArquivo<Doador> implements ICrud<Doador, String> {

    public DoadorRepository() {
        super("data/doador.dat");
    }

    @Override
    public void salvar(Doador entidade) {
        List<Doador> pessoasFisica = buscarTodosArquivo();
        pessoasFisica.add(entidade);
        salvarTodosArquivo(pessoasFisica);
    }

    @Override
    public Doador buscarPorId(String id) {
        List<Doador> pessoas = buscarTodosArquivo();

        Doador pessoa = pessoas.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

        if (pessoa == null) {
            throw new DomainException("Usuário não encontrado");
        }
        return pessoa;
    }

    @Override
    public List<Doador> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Doador entidade) {
        List<Doador> pessoas = buscarTodosArquivo();

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
        List<Doador> pessoasFisicas = buscarTodosArquivo();

        boolean excluir = pessoasFisicas.removeIf(u -> u.getId().equals(id));

        if (!excluir) {
            throw new DomainException("Falha na exclusão, usuário não encontrado!");
        }
        salvarTodosArquivo(pessoasFisicas);
    }
}
