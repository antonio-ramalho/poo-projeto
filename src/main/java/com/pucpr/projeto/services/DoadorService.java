package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Doador;
import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.enums.PerfilUsuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DoadorRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import java.util.List;

public class DoadorService {

    private final UsuarioRepository usuarioRepository;
    private final DoadorRepository doadorRepository;

    public DoadorService(UsuarioRepository repository, DoadorRepository doadorRepository) {
        this.usuarioRepository = repository;
        this.doadorRepository = doadorRepository;
    }

    public void cadastrar(String nome, Cpf cpf, Email email, Telefone telefone, DataNascimento dta, Genero genero,
                          Endereco endereco, String login, String senha, Categoria categoria, boolean anonimato) {

        Usuario usuarioExistente = usuarioRepository.buscarPorLogin(login);
        if (usuarioExistente != null) {
            throw new DomainException("Este login já está em uso por outro usuário.");
        }

        Doador pessoa = new Doador(nome, cpf, genero, dta, endereco, email, telefone, categoria, anonimato);
        Usuario usuario = new Usuario(pessoa.getId(), login, senha, PerfilUsuario.DOADOR);

        doadorRepository.salvar(pessoa);
        usuarioRepository.salvar(usuario);
    }

    public void atualizar(Telefone  telefone, Email email, String nome, Genero genero, String id, Endereco endereco,
                          Categoria categoria, boolean anonimato) {
        Doador pessoa =  doadorRepository.buscarPorId(id);

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

        if  (categoria != null) {
            pessoa.alterarCategoria(categoria);
        }

        pessoa.alterarAnonimato(anonimato);
        doadorRepository.atualizar(pessoa);
    }

    public Doador buscarPorId(String id) {
        return doadorRepository.buscarPorId(id);
    }

    public List<Doador> buscarTodos() {
        return doadorRepository.buscarTodos();
    }

    public void excluir(String id) {
        doadorRepository.excluir(id);
    }
}
