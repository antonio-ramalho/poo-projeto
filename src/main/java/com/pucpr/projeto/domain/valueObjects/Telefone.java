package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Telefone implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String numeroTelefone;

    public Telefone(String numeroTelefone) {
        String numero = Objects.requireNonNull(numeroTelefone, "Telefone é obrigatório")
                .replaceAll("\\D", "");
        validar(numero);
        this.numeroTelefone = numero;
    }

    private void validar(String numeroTelefone) {
        if (numeroTelefone.length() < 10 || numeroTelefone.length() > 11) {
            throw new DomainException("Telefone deve ter 10 ou 11 dígitos (com DDD).");
        }
    }

    public String getNumeroTelefone() {
        return this.numeroTelefone;
    }
}
