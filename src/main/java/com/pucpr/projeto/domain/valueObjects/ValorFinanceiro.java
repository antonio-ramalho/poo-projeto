package com.pucpr.projeto.domain.valueObjects;

import java.io.Serial;
import java.io.Serializable;

public class ValorFinanceiro implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private double valor;

    public ValorFinanceiro(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}