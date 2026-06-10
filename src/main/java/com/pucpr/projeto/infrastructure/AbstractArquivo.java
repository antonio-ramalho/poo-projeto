package com.pucpr.projeto.infrastructure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArquivo<T extends Serializable> {

    protected final String caminhoArquivo;

    public AbstractArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        garantirDiretorioExiste();
    }

    private void garantirDiretorioExiste() {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();


        if (diretorio != null && !diretorio.exists()) {
            boolean criado = diretorio.mkdirs();
            if (criado) {
                System.out.println("Diretório criado com sucesso: " + diretorio.getAbsolutePath());
            }
        }
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
        garantirDiretorioExiste();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            oos.writeObject(registros);
        } catch (IOException e) {
            throw new RuntimeException("Erro grave de I/O ao escrever no arquivo: " + caminhoArquivo, e);
        }
    }
}
