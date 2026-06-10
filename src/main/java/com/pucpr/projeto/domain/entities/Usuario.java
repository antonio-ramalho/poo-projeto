package com.pucpr.projeto.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String id_pessoa;
    private String login;
    private String senha;

    public Usuario(String id_pessoa, String login, String senha) {
        this.id = UUID.randomUUID().toString();
        this.id_pessoa = id_pessoa;
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String id, String id_pessoa, String login, String senha) {
        this.id = id;
        this.id_pessoa = id_pessoa;
        this.login = login;
        this.senha = senha;
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

    public String getIdPessoa() {
        return id_pessoa;
    }

    public String getSenha() {
        return senha;
    }
}
