package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.ValorMonetario;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Despesa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String idOsc;
    private LocalDate data;
    private ValorMonetario valor;
    private String descricao;

    public Despesa(Long id, String idOsc, LocalDate data, ValorMonetario valor, String descricao) {
        this.id = id;
        this.idOsc = idOsc;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdOsc() { return idOsc; }
    public void setIdOsc(String idOsc) { this.idOsc = idOsc; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public ValorMonetario getValor() { return valor; }
    public void setValor(ValorMonetario valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}