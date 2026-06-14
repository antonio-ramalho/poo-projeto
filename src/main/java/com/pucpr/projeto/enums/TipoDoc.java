package com.pucpr.projeto.enums;

public enum TipoDoc {
    ESTATUTO("Estatuto Social"),
    COMPROVANTE_CNPJ("Comprovante de CNPJ"),
    RELATORIO_FINANCEIRO("Relatório Financeiro"),
    OUTROS("Outros");

    private final String descricao;

    TipoDoc(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}