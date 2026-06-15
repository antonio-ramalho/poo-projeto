package com.pucpr.projeto.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Depoimento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String idDoador;
    private String idOsc;
    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;

    public Depoimento(String idDoador, String idOsc, Integer nota, String comentario) {
        this.id = UUID.randomUUID().toString();
        this.idDoador = idDoador;
        this.idOsc = idOsc;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = LocalDate.now();
    }

    public Depoimento(String id, String idDoador, String idOsc, Integer nota, String comentario,
                      LocalDate dataAvaliacao) {
        this.id = id;
        this.idDoador = idDoador;
        this.idOsc = idOsc;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    public void alterarNota(int nota) {
        this.nota = nota;
    }

    public void alterarComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public String getId() {
        return id;
    }

    public String getIdOsc() {
        return idOsc;
    }

    public String getIdDoador() {
        return idDoador;
    }

    public Integer getNota() {
        return nota;
    }
}
