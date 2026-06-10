package com.pucpr.projeto.infrastructure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArquivo<T extends Serializable> {

    protected final String caminhoArquivo;

    public AbstractArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    @SuppressWarnings("unchecked")
    protected List<T> buscarTodosArquivo() {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    protected void salvarTodosArquivo(List<T> registros) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            oos.writeObject(registros);
        } catch (IOException e) {
            throw new RuntimeException("Erro grave de I/O ao escrever no arquivo: " + caminhoArquivo, e);
        }
    }
}
