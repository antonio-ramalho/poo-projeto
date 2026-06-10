package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;

public class Cpf implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String numeroCpf;

    public Cpf(String numeroCpf) {
        seNulo(numeroCpf);
        String cpfLimpo = numeroCpf.replaceAll("\\D", "");
        validarTamanho(cpfLimpo);
        validarDigitos(cpfLimpo);
        this.numeroCpf = cpfLimpo;
    }

    private void seNulo(String numeroCpf) {
        if (numeroCpf == null || numeroCpf.trim().isEmpty()) {
            throw new DomainException("CPF não pode ser nulo ou vazio.");
        }
    }

    private void validarTamanho(String numeroCpf) {
        if (numeroCpf.length() != 11) {
            throw new DomainException("CPF deve conter exatamente 11 dígitos.");
        }
    }

    private void validarDigitos(String numeroCpf) {
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (numeroCpf.charAt(i) - '0') * (10 - i);
        }
        int digito = 11 - (soma % 11);
        if (digito > 9) digito = 0;

        if (digito != (numeroCpf.charAt(9) - '0')) {
            throw new DomainException("Dígito verificador do CPF inválido.");
        }
    }

    public String getNumeroCpf() {
        return this.numeroCpf;
    }
}
