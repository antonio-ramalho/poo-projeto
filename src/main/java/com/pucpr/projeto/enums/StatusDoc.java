package com.pucpr.projeto.enums;

public enum StatusDoc {
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado");

    private final String descricao;

    StatusDoc(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}