package com.pucpr.projeto.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Comentario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String conteudo;
    private LocalDate dataCom;

    public Comentario(Long id, String conteudo, LocalDate dataCom) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataCom = dataCom;
    }

    public void editarTxt(String txt) {
        this.conteudo = txt;
    }

    public Long getId() { return id; }
    public String getConteudo() { return conteudo; }
    public LocalDate getDataCom() { return dataCom; }
}