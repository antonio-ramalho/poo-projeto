package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.UsuarioRepository;
import java.util.List;
import java.util.Objects;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    public Usuario autenticar(String login, String senha) {
        if (Objects.isNull(login) || login.trim().isEmpty() ||
                Objects.isNull(senha) || senha.trim().isEmpty()) {
                throw new DomainException("Por favor, preencha todos os campos de acesso.");
        }

        Usuario usuario = repository.buscarPorLogin(login.trim());

        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new DomainException("Login ou senha incorretos.");
        }
        return usuario;
    }

    public void atualizarSenha(String idUsuario, String novaSenha) {
        Usuario usuario = repository.buscarPorId(idUsuario);

        if (usuario == null) {
            throw new DomainException("Usuário não encontrado.");
        }

        usuario.alterarSenha(novaSenha);

        repository.atualizar(usuario);
    }

    public Usuario buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Usuario> buscarTodos() {
        return repository.buscarTodos();
    }

    public void excluir(String id) {
        repository.excluir(id);
    }
}
