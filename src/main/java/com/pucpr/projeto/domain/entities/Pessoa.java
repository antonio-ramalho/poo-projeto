package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.Email;
import com.pucpr.projeto.domain.valueObjects.Endereco;
import com.pucpr.projeto.domain.valueObjects.Telefone;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public abstract class Pessoa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private Endereco endereco;
    private Email email;
    private Telefone telefone;

    public Pessoa(Endereco endereco, Email email, Telefone telefone) {
        this.id = UUID.randomUUID().toString();
        this.endereco = endereco;
        this.email = email;
        this.telefone = telefone;
    }

    public Pessoa(String id, Endereco endereco, Email email, Telefone telefone) {
        this.id = id;
        this.endereco = endereco;
        this.email = email;
        this.telefone = telefone;
    }

    public void atualizarEndereco(Endereco endereco) {
        this.endereco =  endereco;
    }

    public void atualizarTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public void atualizarTelefone(String telefone) {
        this.telefone = new Telefone(telefone);
    }

    public Email getEmail() {
        return email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getId() {
        return id;
    }

    public Telefone getTelefone() {
        return telefone;
    }
}
