package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.DocOsc;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.PerfilUsuario;
import com.pucpr.projeto.enums.StatusDoc;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.OscRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

public class PessoaJuridicaService {

    private final UsuarioRepository usuarioRepository;
    private final OscRepository oscRepository;

    public PessoaJuridicaService(UsuarioRepository usuarioRepository, OscRepository oscRepository) {
        this.usuarioRepository = usuarioRepository;
        this.oscRepository = oscRepository;
    }

    public void cadastrarOsc(Endereco endereco, Email email, Telefone telefone, Cnpj cnpj,
                             String nomeLegal, String nomeComercial, LocalDate dataFundacao,
                             Categoria atuacao, String chavePix, DocOsc documentoInicial,
                             String login, String senha) {

        if (documentoInicial == null) {
            throw new DomainException("Uma OSC não pode ser cadastrada sem pelo menos um documento comprobatório associado.");
        }

        Usuario usuarioExistente = usuarioRepository.buscarPorLogin(login);
        if (usuarioExistente != null) {
            throw new DomainException("Este login já está em uso por outro usuário.");
        }

        Osc novaOsc = new Osc(endereco, email, telefone, cnpj, nomeLegal, nomeComercial,
                dataFundacao, atuacao, chavePix);

        novaOsc.addDocumento(documentoInicial);
        oscRepository.salvar(novaOsc);

        Usuario usuario = new Usuario(novaOsc.getId(), login, senha, PerfilUsuario.OSC);
        usuarioRepository.salvar(usuario);
    }

    public void validarDocumentoMock(String idOsc, String idDocumento, boolean aprovar) {
        Osc osc = oscRepository.buscarPorId(idOsc);

        boolean docEncontrado = false;
        for (DocOsc doc : osc.getDocumentos()) {
            if (doc.getId().equals(idDocumento)) {
                doc.atualizarStatus(aprovar ? StatusDoc.APROVADO : StatusDoc.REJEITADO);
                docEncontrado = true;
                break;
            }
        }

        if (!docEncontrado) {
            throw new DomainException("O documento solicitado não foi encontrado nesta OSC.");
        }

        osc.calcularTrustScore();
        oscRepository.atualizar(osc);
    }

    public List<Osc> buscarTodas() {
        return oscRepository.buscarTodos();
    }

    public Osc buscarPorId(String idOsc) {
        return oscRepository.buscarPorId(idOsc);
    }

    public void atualizarOsc(Osc osc) {
        oscRepository.atualizar(osc);
    }

    public void excluirOsc(String idOsc) {
        oscRepository.excluir(idOsc);
    }
}