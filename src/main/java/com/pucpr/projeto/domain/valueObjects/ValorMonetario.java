package com.pucpr.projeto.domain.valueObjects;

import com.pucpr.projeto.exceptions.DomainException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ValorMonetario implements Serializable, Comparable<ValorMonetario> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final BigDecimal valor;

    public ValorMonetario(BigDecimal valor) {
        validar(valor);
        this.valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    public ValorMonetario somar(ValorMonetario outro) {
        if (Objects.isNull(outro)) return this;
        return new ValorMonetario(this.valor.add(outro.getValor()));
    }

    public ValorMonetario subtrair(ValorMonetario outro) {
        if (Objects.isNull(outro)) return this;
        return new ValorMonetario(this.valor.subtract(outro.getValor()));
    }

    public ValorMonetario multiplicar(BigDecimal quantidade) {
        if (Objects.isNull(quantidade) || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            return new ValorMonetario(BigDecimal.ZERO);
        }
        return new ValorMonetario(this.valor.multiply(quantidade));
    }

    private void validar(BigDecimal valor) {
        if (Objects.isNull(valor)) {
            throw new DomainException("O valor do preço não pode ser nulo.");
        }

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("O valor do preço não pode ser negativo.");
        }
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public int compareTo(ValorMonetario outro) {
        if (Objects.isNull(outro)) {
            return 1;
        }
        return this.valor.compareTo(outro.getValor());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValorMonetario that = (ValorMonetario) o;
        return this.valor.compareTo(that.valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
