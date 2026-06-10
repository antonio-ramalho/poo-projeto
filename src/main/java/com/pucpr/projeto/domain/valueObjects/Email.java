package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;

public class Email implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String enderecoEmail;

    public Email(String enderecoEmail) {
        validar(enderecoEmail);
        this.enderecoEmail = enderecoEmail;
    }

    private void validar(String enderecoEmail) {
        if (enderecoEmail == null || !enderecoEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new DomainException("E-mail com formato inválido.");
        }
    }

    public String getEnderecoEmail() {
        return this.enderecoEmail;
    }
}
