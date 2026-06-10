package com.pucpr.projeto.enums;

public enum StatusUsuario {
    ATIVO("Ativo"),
    SUSPENSO("Suspenso"),
    CANCELADO("Cancelado"),
    EM_VERIFICACAO("Em verificação");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }
}
