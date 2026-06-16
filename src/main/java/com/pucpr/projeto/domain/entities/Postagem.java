package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.Foto;
import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Postagem extends Publicacao {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Comentario> comentarios;

    public Postagem(Long id, String titulo, LocalDate dataPub, Foto foto, String conteudo) {
        super(id, titulo, dataPub, foto, conteudo);
        this.comentarios = new ArrayList<>();
    }

    public void addComentario(Comentario com) {
        if (com != null) {
            this.comentarios.add(com);
        }
    }

    public List<Comentario> getComentarios() { return comentarios; }
}
