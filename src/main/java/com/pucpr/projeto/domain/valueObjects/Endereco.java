package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Endereco implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Cep numeroCep;
    private final String rua;
    private final String bairro;
    private final String cidade;
    private final String numeroEndereco;

    public Endereco(Cep numeroCep, String rua, String bairro, String cidade, String numeroEndereco) {
        validarEntrada(rua, bairro, cidade, numeroEndereco);
        this.numeroCep = numeroCep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.numeroEndereco = numeroEndereco;
    }

    private void validarEntrada(String rua, String bairro, String cidade, String numeroEndereco) {
        validarTxt(rua, "Rua");
        validarTxt(bairro, "Bairro");
        validarTxt(cidade, "Cidade");
        validarTxt(numeroEndereco, "Número de endereço");
    }

    private void validarTxt(String valor, String tipo) {
        if (Objects.isNull(valor) || valor.trim().isEmpty()) {
            throw new DomainException(tipo + " não pode estar vazio.");
        }
    }

    public Cep getNumeroCep() { return numeroCep; }
    public String getRua() { return rua; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getNumeroEndereco() { return numeroEndereco; }
}
