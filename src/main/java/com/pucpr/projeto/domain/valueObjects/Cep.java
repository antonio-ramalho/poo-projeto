package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Cep implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String numeroCep;

    public Cep(String numeroCep) {
        String numeroLimpo = numeroCep.replaceAll("\\D", "");
        validar(numeroLimpo);
        this.numeroCep = numeroLimpo;
    }

    private void validar(String numeroCep) {
        if (Objects.isNull(numeroCep) || numeroCep.length() != 8) {
            throw new DomainException("CEP inválido. Deve conter 8 dígitos.");
        }
    }

    public String getNumeroCep() {
        return this.numeroCep;
    }
}