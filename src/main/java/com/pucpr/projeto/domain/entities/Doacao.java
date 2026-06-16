package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.ValorMonetario;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Doacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String idOsc;
    private String idDoador; // Novo campo
    private Long id;
    private LocalDate dataDoa;
    private ValorMonetario valor;
    private String mensagem;

    // Construtor atualizado para incluir idOsc e idDoador
    public Doacao(Long id, LocalDate dataDoa, ValorMonetario valor, String mensagem, String idOsc, String idDoador) {
        this.id = id;
        this.dataDoa = dataDoa;
        this.valor = valor;
        this.mensagem = mensagem;
        this.idOsc = idOsc;
        this.idDoador = idDoador;
    }

    public String emitirRecibo() {
        return "Recibo da doação gerado com sucesso.";
    }

    public void cancelarDoacao() {}

    // Getters e Setters
    public String getIdOsc() { return idOsc; }
    public void setIdOsc(String idOsc) { this.idOsc = idOsc; }

    public String getIdDoador() { return idDoador; }
    public void setIdDoador(String idDoador) { this.idDoador = idDoador; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDataDoa() { return dataDoa; }
    public void setDataDoa(LocalDate dataDoa) { this.dataDoa = dataDoa; }
    public ValorMonetario getValor() { return valor; }
    public void setValor(ValorMonetario valor) { this.valor = valor; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}