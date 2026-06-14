package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.StatusDoc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Osc extends PessoaJuridica {

    private Categoria atuacao;
    private String chavePix;
    private Double trustScore;
    private List<DocOsc> documentos;

    public Osc(Endereco endereco, Email email, Telefone telefone,
               Cnpj cnpj, String nomeLegal, String nomeComercial, LocalDate dataFundacao,
               Categoria atuacao, String chavePix) {
        super(endereco, email, telefone, cnpj, nomeLegal, nomeComercial, dataFundacao);
        this.atuacao = atuacao;
        this.chavePix = chavePix;
        this.trustScore = 0.0;
        this.documentos = new ArrayList<>();
    }

    public Osc(String id, Endereco endereco, Email email, Telefone telefone,
               Cnpj cnpj, String nomeLegal, String nomeComercial, LocalDate dataFundacao,
               Categoria atuacao, String chavePix) {
        super(id, endereco, email, telefone, cnpj, nomeLegal, nomeComercial, dataFundacao);
        this.atuacao = atuacao;
        this.chavePix = chavePix;
        this.trustScore = 0.0;
        this.documentos = new ArrayList<>();
    }

    public void addDocumento(DocOsc doc) {
        if (doc != null) {
            this.documentos.add(doc);
        }
    }

    public Double calcularTrustScore() {
        long docAprovados = documentos.stream()
                .filter(d -> d.getStatus() == StatusDoc.APROVADO)
                .count();
        this.trustScore = docAprovados * 10.0;
        return this.trustScore;
    }

    public Categoria getAtuacao() { return atuacao; }
    public String getChavePix() { return chavePix; }
    public Double getTrustScore() { return trustScore; }
    public List<DocOsc> getDocumentos() { return documentos; }
}