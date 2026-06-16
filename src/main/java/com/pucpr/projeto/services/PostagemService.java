package com.pucpr.projeto.services;

import com.pucpr.projeto.domain.entities.Postagem;
import com.pucpr.projeto.domain.entities.Comentario;
import com.pucpr.projeto.repositories.PostagemRepository;
import java.util.List;

public class PostagemService {

    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    public void criarPostagem(Postagem postagem) {
        postagemRepository.salvar(postagem);
    }

    public List<Postagem> listarTodas() {
        return postagemRepository.buscarTodos();
    }

    public void adicionarComentario(Long idPostagem, Comentario comentario) {
        Postagem postagem = postagemRepository.buscarPorId(idPostagem);
        postagem.addComentario(comentario);
        postagemRepository.atualizar(postagem);
    }
}
