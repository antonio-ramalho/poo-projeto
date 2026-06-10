package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;

public class Foto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String url;

    public Foto(String url) {
        validarFormato(url);
        this.url = url;
    }

    private void validarFormato(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new DomainException("A URL da foto não pode ser vazia.");
        }
        if (!url.matches(".*\\.(png|jpg|jpeg|gif)$")) {
            throw new DomainException("O formato da foto deve ser PNG, JPG, JPEG ou GIF.");
        }
    }

    public String getUrl() {
        return this.url;
    }
}
