package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.enums.StatusDoc;
import com.pucpr.projeto.enums.TipoDoc;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class DocOsc implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private LocalDate dataEmissao;
    private TipoDoc tipo;
    private String urlArquivo;
    private StatusDoc status;

    public DocOsc(LocalDate dataEmissao, TipoDoc tipo, String urlArquivo) {
        this.id = UUID.randomUUID().toString();
        this.dataEmissao = dataEmissao;
        this.tipo = tipo;
        this.urlArquivo = urlArquivo;
        this.status = StatusDoc.PENDENTE;
    }

    public void atualizarStatus(StatusDoc sts) {
        this.status = sts;
    }

    public String getId() { return id; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public TipoDoc getTipo() { return tipo; }
    public String getUrlArquivo() { return urlArquivo; }
    public StatusDoc getStatus() { return status; }
}