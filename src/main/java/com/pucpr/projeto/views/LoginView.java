package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.PessoaFisicaRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.PessoaFisicaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private final Stage stage;
    private final UsuarioService usuarioService;
    private Scene scene;

    public LoginView(Stage stage, UsuarioService usuarioService) {
        this.stage = stage;
        this.usuarioService = usuarioService;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField txtLogin = new TextField();
        PasswordField txtSenha = new PasswordField();
        Button btnEntrar = new Button("Entrar");
        Button btnCadastrarDoador = new Button("Cadastrar como Doador (Pessoa Física)");
        Button btnCadastrarOsc = new Button("Cadastrar OSC (Pessoa Jurídica)");

        btnEntrar.setOnAction(e -> {
            try {
                Usuario usuarioLogado = usuarioService.autenticar(txtLogin.getText(), txtSenha.getText());
                irParaHome(usuarioLogado);
            } catch (DomainException ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
            }
        });

        btnCadastrarDoador.setOnAction(e -> irParaCadastroDoador());

        btnCadastrarOsc.setOnAction(e -> irParaCadastroOsc());

        layout.getChildren().addAll(
                new Label("Login:"), txtLogin,
                new Label("Senha:"), txtSenha,
                btnEntrar, new Separator(),
                btnCadastrarDoador, btnCadastrarOsc
        );
        this.scene = new Scene(layout, 400, 350);
    }

    public Scene getScene() { return this.scene; }

    private void irParaCadastroDoador() {
        UsuarioRepository uRepo = new UsuarioRepository();
        PessoaFisicaRepository pRepo = new PessoaFisicaRepository();
        PessoaFisicaService cadastroService = new PessoaFisicaService(uRepo, pRepo);

        CadastroDoadorView cadastroView = new CadastroDoadorView(stage, cadastroService);
        stage.setScene(cadastroView.getScene());
    }

    private void irParaHome(Usuario usuarioLogado) {
        HomeView homeView = new HomeView(stage, usuarioLogado);
        stage.setScene(homeView.getScene());
    }

    private void irParaCadastroOsc() {
        com.pucpr.projeto.repositories.UsuarioRepository uRepo = new com.pucpr.projeto.repositories.UsuarioRepository();
        com.pucpr.projeto.repositories.OscRepository oRepo = new com.pucpr.projeto.repositories.OscRepository();
        com.pucpr.projeto.services.PessoaJuridicaService cadastroService = new com.pucpr.projeto.services.PessoaJuridicaService(uRepo, oRepo);

        CadastroOscView cadastroView = new CadastroOscView(stage, cadastroService);
        stage.setScene(cadastroView.getScene());
    }
}
