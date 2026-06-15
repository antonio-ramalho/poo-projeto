package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Depoimento;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DepoimentoRepository;
import java.util.List;

public class DepoimentoService {

    private final DepoimentoRepository repository;

    public DepoimentoService(DepoimentoRepository repository) {
        this.repository = repository;
    }

    public void avaliar(String idDoador, String idOsc, Integer nota, String comentario) {

        if (idDoador == null || idDoador.isEmpty() || idOsc == null || idOsc.isEmpty()) {
            throw new DomainException("Para cadastrar um depoimento é preciso do id da osc e do doador.");
        }

        if (nota == null || nota < 1 || nota > 5) {
            throw new DomainException("É preciso cadastrar uma nota de 1 a 5.");
        }

        Depoimento depoimento = new Depoimento(idDoador, idOsc, nota, comentario);

        repository.salvar(depoimento);
    }

    public void atualizar(String id, Integer nota, String comentario) {

        Depoimento depoimento = repository.buscarPorId(id);

        if (nota != null) {
            depoimento.alterarNota(nota);
        }

        if (comentario != null) {
            depoimento.alterarComentario(comentario);
        }

        repository.atualizar(depoimento);
    }

    public List<Depoimento> buscarPorDoador(String idDoador) {
        return repository.buscarTodos().stream()
                .filter(d -> d.getIdDoador().equals(idDoador))
                .toList();
    }

    public Depoimento buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Depoimento> buscarTodos() {
        return repository.buscarTodos();
    }

    public void excluir(String id) {
        repository.excluir(id);
    }
}
