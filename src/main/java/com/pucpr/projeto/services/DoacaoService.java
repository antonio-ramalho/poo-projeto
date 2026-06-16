package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Doacao;
import com.pucpr.projeto.domain.valueObjects.ValorMonetario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DoacaoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DoacaoService {
    private final DoacaoRepository repository;

    public DoacaoService(DoacaoRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(LocalDate data, ValorMonetario valor, String mensagem, String idOsc, String idDoador) {
        if (data.isAfter(LocalDate.now())) {
            throw new DomainException("A data da doação não pode ser futura.");
        }

        Long idGerado = System.currentTimeMillis();

        // Agora passando idOsc e idDoador
        Doacao novaDoacao = new Doacao(idGerado, data, valor, mensagem, idOsc, idDoador);
        repository.salvar(novaDoacao);
    }

    public List<Doacao> buscarTodos() {
        return repository.buscarTodos();
    }

    // Método corrigido para evitar o erro de NullPointerException
    public List<Doacao> buscarPorDoador(String idDoador) {
        return repository.buscarTodos().stream()
                .filter(d -> d.getIdDoador() != null && d.getIdDoador().equals(idDoador))
                .collect(Collectors.toList());
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }
}