package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.enums.StatusUsuario;

public class Doador extends PessoaFisica{

    private Categoria categoria;
    private boolean anonimato;
    private StatusUsuario status;

    public Doador (String nome, Cpf cpf, Genero genero, DataNascimento dta, Endereco endereco, Email email,
                   Telefone telefone, Categoria categoria, boolean isAnonimato) {
        super(nome, cpf, genero, dta, endereco, email, telefone);
        this.categoria = categoria;
        this.anonimato = isAnonimato;
        this.status = StatusUsuario.ATIVO;
    }

    public Doador (String id, String nome, Cpf cpf, Genero genero, DataNascimento dta, Endereco endereco, Email email,
                   Telefone telefone, Categoria categoria, boolean anonimato) {
        super(id, nome, cpf, genero, dta, endereco, email, telefone);
        this.categoria = categoria;
        this.anonimato = anonimato;
        this.status = StatusUsuario.ATIVO;
    }

    public void comentar() {

    }

    public void doar() {

    }

    public boolean getAnonimato() {
        return anonimato;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public StatusUsuario getStatus() {
        return status;
    }
}
