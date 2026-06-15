package com.pucpr.projeto.infrastructure;

import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.enums.PerfilUsuario;
import com.pucpr.projeto.repositories.PessoaFisicaRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;

public class AdmCadastro {
    public static void main(String[] args) {

       String nome =  "Antonio Ramalho";
       Cpf cpf = new Cpf("11174448997");
       Email email = new Email("antonio.ramalho@gmail.com");
       Telefone telefone = new Telefone("42984273174");
       Genero genero = Genero.MASCULINO;
       DataNascimento dataNascimento = new DataNascimento("01/02/2007");
       Endereco endereco = new Endereco(new Cep("85195000"), "Das flores", "Centro",
               "Curitiba", "1394");

       String usuario = "antonio@adm";
       String senha = "1234";

       PessoaFisica pessoa = new PessoaFisica(nome, cpf, genero, dataNascimento, endereco, email, telefone);
       Usuario usuarioPessoa = new Usuario(pessoa.getId(), usuario, senha, PerfilUsuario.ADMINISTRADOR);

       PessoaFisicaRepository pessoaRepository = new PessoaFisicaRepository();
       UsuarioRepository usuarioRepository = new UsuarioRepository();

       pessoaRepository.salvar(pessoa);
       usuarioRepository.salvar(usuarioPessoa);
    }
}
