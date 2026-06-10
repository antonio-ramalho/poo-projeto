package com.pucpr.projeto.repositories;

import com.pucpr.projeto.infrastructure.AbstractArquivo;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.interfaces.ICrud;
import java.util.List;

public class UsuarioRepository extends AbstractArquivo<Usuario> implements ICrud<Usuario, String> {

    public UsuarioRepository() {
        super("data/usuarios.dat");
    }

    @Override
    public Usuario buscarPorId(String id) {
        List<Usuario> usuarios = buscarTodosArquivo();

        Usuario usuario = usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

        if (usuario == null) {
            throw new DomainException("Usuário não encontrado");
        }
        return usuario;
    }

    @Override
    public List<Usuario> buscarTodos() {
        return buscarTodosArquivo();
    }

    @Override
    public void atualizar(Usuario entidade) {
        List<Usuario> usuarios = buscarTodosArquivo();

        for (int i = 0; i < usuarios.size(); i++) {

            if (usuarios.get(i).getId().equals(entidade.getId())) {
                usuarios.set(i, entidade);
                salvarTodosArquivo(usuarios);
                return;
            }
        }

        throw new DomainException("Não foi possível concluir a atualização! Usuário não encontrado.");
    }

    @Override
    public void salvar(Usuario usuario) {
        List<Usuario> usuarios = buscarTodosArquivo();
        usuarios.add(usuario);
        salvarTodosArquivo(usuarios);
    }

    @Override
    public void excluir(String id) {
        List<Usuario> usuarios = buscarTodosArquivo();

        boolean excluir = usuarios.removeIf(u -> u.getId().equals(id));

        if (!excluir) {
            throw new DomainException("Falha na exclusão, usuário não encontrado!");
        }
        salvarTodosArquivo(usuarios);
    }

    public Usuario buscarPorLogin(String login) {
        List<Usuario> usuarios = buscarTodosArquivo();
        return  usuarios.stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst()
                .orElse(null);
    }
}
