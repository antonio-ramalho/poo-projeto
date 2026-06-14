package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.enums.PerfilUsuario;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String senha;
    private PerfilUsuario perfil;

    public Usuario(String id, String login, String senha, PerfilUsuario perfil) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public PerfilUsuario getPerfil() { return perfil; }
}
