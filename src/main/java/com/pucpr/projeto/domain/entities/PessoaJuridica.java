package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.*;
import java.time.LocalDate;

public class PessoaJuridica extends Pessoa {

    private Cnpj cnpj;
    private String nomeLegal;
    private String nomeComercial;
    private LocalDate dataFundacao;

    public PessoaJuridica(Endereco endereco, Email email, Telefone telefone,
                          Cnpj cnpj, String nomeLegal, String nomeComercial, LocalDate dataFundacao) {
        super(endereco, email, telefone);
        this.cnpj = cnpj;
        this.nomeLegal = nomeLegal;
        this.nomeComercial = nomeComercial;
        this.dataFundacao = dataFundacao;
    }

    public PessoaJuridica(String id, Endereco endereco, Email email, Telefone telefone,
                          Cnpj cnpj, String nomeLegal, String nomeComercial, LocalDate dataFundacao) {
        super(id, endereco, email, telefone);
        this.cnpj = cnpj;
        this.nomeLegal = nomeLegal;
        this.nomeComercial = nomeComercial;
        this.dataFundacao = dataFundacao;
    }

    public void alterarNomeLegal(String nomeLegal) {
        this.nomeLegal = nomeLegal;
    }

    public void alterarNomeMarca(String nomeComercial) {
        this.nomeComercial = nomeComercial;
    }

    public Cnpj getCnpj() { return cnpj; }
    public String getNomeLegal() { return nomeLegal; }
    public String getNomeComercial() { return nomeComercial; }
    public LocalDate getDataFundacao() { return dataFundacao; }
}
