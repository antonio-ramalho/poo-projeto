package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataNascimento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate dataNascimento;

    public DataNascimento(String dataNascimento) {
        seNull(dataNascimento);
        LocalDate data = converter(dataNascimento);
        validarDataFutura(data);
        validarIdadeMinina(data);
        this.dataNascimento = data;
    }

    private LocalDate converter(String dataNascimento) {
        try {
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataNascimento, formatador);
        } catch (DateTimeParseException e) {
            throw new DomainException("Formato de data inválido. Utilize o padrão brasileiro: DD/MM/AAAA.");
        }
    }

    private void seNull(String dataNascimento) {
        if (dataNascimento == null) {
            throw new DomainException("A data de nascimento não pode ser nula.");
        }
    }

    private void validarDataFutura(LocalDate data) {
        if (data.isAfter(LocalDate.now())) {
            throw new DomainException("A data de nascimento não pode estar no futuro.");
        }
    }

    private void validarIdadeMinina(LocalDate data) {
        int idade = Period.between(data, LocalDate.now()).getYears();
        if (idade < 18) {
            throw new DomainException("A idade mínima não foi atingida.");
        }
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getFormatada() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataNascimento.format(formatador);
    }
}
