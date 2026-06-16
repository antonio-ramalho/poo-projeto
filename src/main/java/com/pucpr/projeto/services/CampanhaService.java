package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Campanha;
import com.pucpr.projeto.domain.valueObjects.ValorFinanceiro;
import com.pucpr.projeto.repositories.CampanhaRepository;
import java.util.List;

public class CampanhaService {

    private final CampanhaRepository campanhaRepository;

    public CampanhaService(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    public void cadastrarCampanha(Campanha campanha) {
        campanhaRepository.salvar(campanha);
    }

    public List<Campanha> listarTodas() {
        return campanhaRepository.buscarTodos();
    }

    public void registrarDoacao(Long idCampanha, ValorFinanceiro valor) {
        Campanha campanha = campanhaRepository.buscarPorId(idCampanha);
        campanha.addDoacao(valor);

        if (campanha.verificarMeta()) {

            campanha.encerrarCampanha();
        }

        campanhaRepository.atualizar(campanha);
    }

    public void deletarCampanha(Long id) {
        campanhaRepository.excluir(id);
    }
}