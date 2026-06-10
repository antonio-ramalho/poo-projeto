package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Genero;

public class PessoaFisica extends Pessoa {

    private String nome;
    private Cpf cpf;
    private Genero genero;
    private DataNascimento dataNascimento;

    public PessoaFisica(String nome, Cpf cpf, Genero genero, DataNascimento dta,
                        Endereco endereco, Email email, Telefone telefone) {
        super(endereco, email, telefone);
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.dataNascimento = dta;
    }

    public PessoaFisica(String id, String nome, Cpf cpf, Genero genero, DataNascimento dta,
                        Endereco endereco, Email email, Telefone telefone) {
        super(id, endereco, email, telefone);
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.dataNascimento = dta;
    }

    public void alterarNome(String nome){
        this.nome = nome;
    }

    public void alterarGenero(Genero genero){
        this.genero = genero;
    }

    public DataNascimento getDataNascimento() {
        return dataNascimento;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Genero getGenero() {
        return genero;
    }

    public String getNome() {
        return nome;
    }
}
