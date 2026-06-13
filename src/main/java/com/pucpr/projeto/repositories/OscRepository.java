package com.pucpr.projeto.repositories;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.interfaces.ICrud;

import java.util.List;

public class OscRepository extends AbstractArquivo<Osc> implements ICrud<Osc, String> {

    public OscRepository() {
        super("data/osc.dat");
    }

    @Override
    public void salvar(Osc entidade) {
        List<Osc> oscs = buscarTodosArquivo();
        oscs.add(entidade);
        salvarTodosArquivo(oscs);
    }

    @Override
    public Osc buscarPorId(String id) {
        List<Osc> oscs = buscarTodosArquivo();
        return oscs.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DomainException("OSC não encontrada."));
    }

    @Override
    public List<Osc> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Osc entidade) {
        List<Osc> oscs = buscarTodosArquivo();
        for (int i = 0; i < oscs.size(); i++) {
            if (oscs.get(i).getId().equals(entidade.getId())) {
                oscs.set(i, entidade);
                salvarTodosArquivo(oscs);
                return;
            }
        }
        throw new DomainException("Não foi possível concluir a atualização! OSC não encontrada.");
    }

    @Override
    public void excluir(String id) {
        List<Osc> oscs = buscarTodosArquivo();
        boolean removido = oscs.removeIf(o -> o.getId().equals(id));
        if (!removido) {
            throw new DomainException("Falha na exclusão, OSC não encontrada!");
        }
        salvarTodosArquivo(oscs);
    }
}