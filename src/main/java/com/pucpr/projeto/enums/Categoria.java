package com.pucpr.projeto.enums;

public enum Categoria {
    SAUDE("Saúde"),
    EDUCACAO("Educação"),
    SERVICO_SOCIAL("Serviço social"),
    MEIO_AMBIENTE("Meio ambiente");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
