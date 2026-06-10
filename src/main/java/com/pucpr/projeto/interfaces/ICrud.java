package com.pucpr.projeto.interfaces;

import java.util.List;

public interface ICrud<T, ID> {

    void salvar(T entidade);
    T buscarPorId(ID id);
    List<T> buscarTodos();
    void atualizar(T entidade);
    void excluir(ID id);
}
