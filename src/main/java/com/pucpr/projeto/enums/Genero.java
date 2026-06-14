package com.pucpr.projeto.enums;

public enum Genero {
    FEMININO("Feminino"),
    MASCULINO("Masculino");

    private final String descricao;

    public String toUpperCase() {
        return  descricao.toUpperCase();
    }

    Genero(String descricao) {
        this.descricao = descricao;
    }
}
