package com.pucpr.projeto.enums;

public enum PerfilUsuario {
    ADMINISTRADOR("Administrador"),
    DOADOR("Doador"),
    OSC("Osc");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
