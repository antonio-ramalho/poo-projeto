package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.Foto;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Publicacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String titulo;
    private LocalDate dataPub;
    private Foto foto;
    private String conteudo;

    public Publicacao(Long id, String titulo, LocalDate dataPub, Foto foto, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.dataPub = dataPub;
        this.foto = foto;
        this.conteudo = conteudo;
    }

    public void editarConteudo(String txt) { this.conteudo = txt; }
    public void alterarTitulo(String titu) { this.titulo = titu; }


    public void alterarFoto(Foto fotoAtualizada) { this.foto = fotoAtualizada; }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public LocalDate getDataPub() { return dataPub; }
    public Foto getFoto() { return foto; }
    public String getConteudo() { return conteudo; }
}