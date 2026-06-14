package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.enums.PerfilUsuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.PessoaFisicaRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import java.util.List;

public class PessoaFisicaService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaFisicaRepository pessoaRepository;

    public PessoaFisicaService(UsuarioRepository repository, PessoaFisicaRepository pessoaRepository) {
        this.usuarioRepository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public void cadastrar(String nome, Cpf cpf, Email email, Telefone telefone, DataNascimento dta, Genero genero,
                          Endereco endereco, String login, String senha) {

        Usuario usuarioExistente = usuarioRepository.buscarPorLogin(login);
        if (usuarioExistente != null) {
            throw new DomainException("Este login já está em uso por outro usuário.");
        }

        PessoaFisica pessoa = new PessoaFisica(nome, cpf, genero, dta, endereco, email, telefone);
        Usuario usuario = new Usuario(pessoa.getId(), login, senha, PerfilUsuario.DOADOR);

        pessoaRepository.salvar(pessoa);
        usuarioRepository.salvar(usuario);
    }

    public void atualizar(Telefone  telefone, Email email, String nome, Genero genero, String id, Endereco endereco) {
        PessoaFisica pessoa =  pessoaRepository.buscarPorId(id);

        if (pessoa == null) {
            throw new DomainException("Essa pessoa não está cadastrada.");
        }

        if (telefone != null) {
            pessoa.atualizarTelefone(telefone);
        }

        if (email != null) {
            pessoa.atualizarEmail(email);
        }

        if (nome != null) {
            pessoa.alterarNome(nome);
        }

        if (genero != null) {
            pessoa.alterarGenero(genero);
        }

        if (endereco != null) {
            pessoa.atualizarEndereco(endereco);
        }

        pessoaRepository.atualizar(pessoa);
    }

    public PessoaFisica buscarPorId(String id) {
        return pessoaRepository.buscarPorId(id);
    }

    public List<PessoaFisica> buscarTodos() {
        return pessoaRepository.buscarTodos();
    }

    public void excluir(String id) {
        pessoaRepository.excluir(id);
    }
}
