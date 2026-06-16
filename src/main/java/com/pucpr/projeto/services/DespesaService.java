package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Despesa;
import com.pucpr.projeto.domain.valueObjects.ValorMonetario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DespesaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DespesaService {
    private final DespesaRepository repository;

    public DespesaService(DespesaRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(LocalDate data, ValorMonetario valor, String descricao, String idOsc) {
        if (data.isAfter(LocalDate.now())) {
            throw new DomainException("A data da despesa não pode ser futura.");
        }

        Long idGerado = System.currentTimeMillis();
        Despesa novaDespesa = new Despesa(idGerado, idOsc, data, valor, descricao);
        repository.salvar(novaDespesa);
    }

    public List<Despesa> buscarPorOsc(String idOsc) {
        return repository.buscarTodos().stream()
                .filter(d -> d.getIdOsc().equals(idOsc))
                .collect(Collectors.toList());
    }

    public List<Despesa> buscarTodos() {
        return repository.buscarTodos();
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }
}