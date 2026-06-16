package com.pucpr.projeto.domain.entities;

import com.pucpr.projeto.domain.valueObjects.Foto;
import com.pucpr.projeto.domain.valueObjects.ValorFinanceiro;
import com.pucpr.projeto.enums.StatusCampanha;
import java.io.Serial;
import java.time.LocalDate;

public class Campanha extends Publicacao {
    @Serial
    private static final long serialVersionUID = 1L;

    private ValorFinanceiro meta;
    private StatusCampanha status;
    private LocalDate dataEnce;
    private double totalArrecadado;

    public Campanha(Long id, String titulo, LocalDate dataPub, Foto foto, String conteudo,
                    ValorFinanceiro meta, StatusCampanha status, LocalDate dataEnce) {
        super(id, titulo, dataPub, foto, conteudo);
        this.meta = meta;
        this.status = status;
        this.dataEnce = dataEnce;
        this.totalArrecadado = 0.0;
    }

    public void encerrarCampanha() {
        this.status = StatusCampanha.ENCERRADA;
    }

    public boolean verificarMeta() {
        return this.totalArrecadado >= meta.getValor();
    }

    public void addDoacao(ValorFinanceiro valor) {
        if (valor != null) {
            this.totalArrecadado += valor.getValor();
        }
    }

    public void alterarDataEncerramento(LocalDate dta) {
        this.dataEnce = dta;
    }

    public ValorFinanceiro getMeta() { return meta; }
    public StatusCampanha getStatus() { return status; }
    public LocalDate getDataEnce() { return dataEnce; }
    public double getTotalArrecadado() { return totalArrecadado; }
}