package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.enums.PerfilUsuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DoadorRepository;
import com.pucpr.projeto.repositories.OscRepository;
import com.pucpr.projeto.repositories.PessoaFisicaRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.repositories.DespesaRepository;
import com.pucpr.projeto.services.DoadorService;
import com.pucpr.projeto.services.PessoaJuridicaService;
import com.pucpr.projeto.services.UsuarioService;
import com.pucpr.projeto.services.DespesaService;
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
        Button btnCadastrarDoador = new Button("Cadastrar como Doador");
        Button btnCadastrarOsc = new Button("Cadastrar OSC");

        btnEntrar.setOnAction(e -> {
            try {
                Usuario usuarioLogado = usuarioService.autenticar(txtLogin.getText(), txtSenha.getText());
                rotearParaHomeCorreta(usuarioLogado);
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

    private void rotearParaHomeCorreta(Usuario usuarioLogado) {
        PerfilUsuario perfil = usuarioLogado.getPerfil();

        if (perfil == PerfilUsuario.ADMINISTRADOR) {
            irParaHomeAdm(usuarioLogado);
            return;
        }

        if (perfil == PerfilUsuario.DOADOR) {
            HomeDoadorView home = new HomeDoadorView(stage, usuarioLogado);
            stage.setScene(home.getScene());
            return;
        }

        if (perfil == PerfilUsuario.OSC) {
            PessoaJuridicaService pjService = new PessoaJuridicaService(new UsuarioRepository(), new OscRepository());
            Osc osc = pjService.buscarPorId(usuarioLogado.getId());

            // Inicializando dependências para o Módulo Financeiro
            DespesaRepository despesaRepository = new DespesaRepository();
            DespesaService despesaService = new DespesaService(despesaRepository);

            HomeOscView home = new HomeOscView(stage, usuarioLogado, osc, despesaService);
            stage.setScene(home.getScene());
            return;
        }

        new Alert(Alert.AlertType.ERROR, "Perfil de usuário não reconhecido.").showAndWait();
    }

    private void irParaCadastroDoador() {
        DoadorService cadastroService = new DoadorService(new UsuarioRepository(), new DoadorRepository());
        CadastroDoadorView cadastroView = new CadastroDoadorView(stage, cadastroService);
        stage.setScene(cadastroView.getScene());
    }

    private void irParaCadastroOsc() {
        PessoaJuridicaService cadastroService = new PessoaJuridicaService(new UsuarioRepository(), new OscRepository());
        CadastroOscView cadastroView = new CadastroOscView(stage, cadastroService);
        stage.setScene(cadastroView.getScene());
    }

    private void irParaHomeAdm(Usuario admin) {
        HomeAdmView admView = new HomeAdmView(stage, admin);
        stage.setScene(admView.getScene());
    }
}