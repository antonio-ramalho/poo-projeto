package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.PessoaFisicaRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;

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
        Usuario usuario = new Usuario(pessoa.getId(), login, senha);

        pessoaRepository.salvar(pessoa);
        usuarioRepository.salvar(usuario);
    }
}
