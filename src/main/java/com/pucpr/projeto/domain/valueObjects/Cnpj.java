package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;

public class Cnpj implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String numeroCnpj;

    public Cnpj(String numeroCnpj) {
        seNull(numeroCnpj);
        String cnpjLimpo = numeroCnpj.replaceAll("\\D", "");
        validarTamanho(cnpjLimpo);
        this.numeroCnpj = cnpjLimpo;
    }

    private void seNull(String numeroCnpj) {
        if (numeroCnpj == null || numeroCnpj.trim().isEmpty()) {
            throw new DomainException("CNPJ não pode ser nulo ou vazio.");
        }
    }

    private void validarTamanho(String numeroCnpj) {
        if (numeroCnpj.replaceAll("\\D", "").length() != 14) {
            throw new DomainException("CNPJ deve conter exatamente 14 dígitos.");
        }
    }

    public String getNumeroCnpj() {
        return this.numeroCnpj;
    }
}
