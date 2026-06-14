package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Doador;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DoadorRepository;
import com.pucpr.projeto.repositories.OscRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.DoadorService;
import com.pucpr.projeto.services.PessoaJuridicaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HomeAdmView {
    private final Stage stage;
    private final Usuario adminLogado;
    private Scene scene;
    private PessoaJuridicaService oscService;
    private DoadorService doadorService;
    private UsuarioService usuarioService;

    public HomeAdmView(Stage stage, Usuario adminLogado) {
        this.stage = stage;
        this.adminLogado = adminLogado;

        this.usuarioService = new UsuarioService(new UsuarioRepository());
        this.oscService = new PessoaJuridicaService(new UsuarioRepository(), new OscRepository());
        this.doadorService = new DoadorService(new UsuarioRepository(), new DoadorRepository());

        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label titulo = new Label("Painel Administrativo - Bem-vindo(a) " + adminLogado.getLogin());
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnSair = new Button("Fazer Logout");
        btnSair.setOnAction(e -> fazerLogout());

        TabPane painelAbas = new TabPane();
        painelAbas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        painelAbas.getTabs().add(criarAbaOscs());
        painelAbas.getTabs().add(criarAbaDoadores());
        painelAbas.getTabs().add(criarAbaUsuarios());

        layout.getChildren().addAll(titulo, painelAbas, btnSair);
        this.scene = new Scene(layout, 1000, 600);
    }

    private Tab criarAbaOscs() {
        Tab aba = new Tab("Gestão de OSCs");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<Osc> tabela = new TableView<>();

        TableColumn<Osc, String> colNome = new TableColumn<>("Nome Fantasia");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomeComercial()));

        TableColumn<Osc, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCnpj() != null ? data.getValue().getCnpj().getNumeroCnpj() : ""));

        TableColumn<Osc, String> colEmail = new TableColumn<>("E-mail Contato");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail().getEnderecoEmail()));

        TableColumn<Osc, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone().getNumeroTelefone()));

        TableColumn<Osc, String> colCidade = new TableColumn<>("Sede (Cidade)");
        colCidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco().getCidade()));

        TableColumn<Osc, String> colAtuacao = new TableColumn<>("Área de Atuação");
        colAtuacao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtuacao() != null ? data.getValue().getAtuacao().name() : ""));

        tabela.getColumns().addAll(colNome, colCnpj, colEmail, colTelefone, colCidade, colAtuacao);
        tabela.getItems().setAll(oscService.buscarTodas());

        Button btnEditar = new Button("Editar Selecionada");
        btnEditar.setOnAction(e -> {
            Osc selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                EdicaoOscAdmView edicaoView = new EdicaoOscAdmView(stage, selecionada, oscService, adminLogado);
                stage.setScene(edicaoView.getScene());
            } else {
                new Alert(Alert.AlertType.WARNING, "Selecione uma OSC na tabela primeiro.").showAndWait();
            }
        });

        Button btnExcluir = new Button("Excluir OSC");
        btnExcluir.setStyle("-fx-text-fill: red;");
        btnExcluir.setOnAction(e -> {
            Osc selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                oscService.excluirOsc(selecionada.getId());
                usuarioService.excluir(selecionada.getId());
                tabela.getItems().setAll(oscService.buscarTodas());
            }
        });

        HBox botoes = new HBox(10, btnEditar, btnExcluir);
        layout.getChildren().addAll(new Label("OSCs Cadastradas (Detalhado):"), tabela, botoes);
        aba.setContent(layout);
        return aba;
    }

    private Tab criarAbaDoadores() {
        Tab aba = new Tab("Gestão de Doadores");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<Doador> tabela = new TableView<>();

        TableColumn<Doador, String> colNome = new TableColumn<>("Nome Completo");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Doador, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCpf().getNumeroCpf() != null ? data.getValue().getCpf().getNumeroCpf() : ""));

        TableColumn<Doador, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail().getEnderecoEmail()));

        TableColumn<Doador, String> colDataNasc = new TableColumn<>("Nascimento");
        colDataNasc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDataNascimento().getFormatada()));

        TableColumn<Doador, String> colCidade = new TableColumn<>("Cidade");
        colCidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco().getCidade() != null ? data.getValue().getEndereco().getCidade() : "Não informada"));

        TableColumn<Doador, String> colPreferencia = new TableColumn<>("Causa Preferida");
        colPreferencia.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria().getDescricao() != null ? data.getValue().getCategoria().name() : "Geral"));

        tabela.getColumns().addAll(colNome, colCpf, colEmail, colDataNasc, colCidade, colPreferencia);
        tabela.getItems().setAll(doadorService.buscarTodos());

        Button btnEditar = new Button("Editar Selecionado");
        btnEditar.setOnAction(e -> {
            Doador selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                EdicaoDoadorAdmView edicaoView = new EdicaoDoadorAdmView(stage, selecionado, doadorService, adminLogado);
                stage.setScene(edicaoView.getScene());
            } else {
                new Alert(Alert.AlertType.WARNING, "Selecione um Doador na tabela primeiro.").showAndWait();
            }
        });

        Button btnExcluir = new Button("Excluir Doador");
        btnExcluir.setStyle("-fx-text-fill: red;");
        btnExcluir.setOnAction(e -> {
            Doador selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                doadorService.excluir(selecionado.getId());
                usuarioService.excluir(selecionado.getId());
                tabela.getItems().setAll(doadorService.buscarTodos());
            }
        });

        HBox botoes = new HBox(10, btnEditar, btnExcluir);
        layout.getChildren().addAll(new Label("Doadores Cadastrados (Detalhado):"), tabela, botoes);
        aba.setContent(layout);
        return aba;
    }

    private Tab criarAbaUsuarios() {
        Tab aba = new Tab("Gestão de Acessos");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TableView<Usuario> tabela = new TableView<>();
        TableColumn<Usuario, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));

        TableColumn<Usuario, String> colLogin = new TableColumn<>("Login");
        colLogin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogin()));

        TableColumn<Usuario, String> colPerfil = new TableColumn<>("Perfil");
        colPerfil.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPerfil().getDescricao()));

        tabela.getColumns().addAll(colId, colLogin, colPerfil);
        tabela.getItems().setAll(usuarioService.buscarTodos());

        TextField txtNovaSenha = new TextField();
        txtNovaSenha.setPromptText("Nova Senha");
        Button btnMudarSenha = new Button("Forçar Troca de Senha");

        btnMudarSenha.setOnAction(e -> {
            Usuario selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null && !txtNovaSenha.getText().isEmpty()) {
                try {
                    usuarioService.atualizarSenha(selecionado.getId(), txtNovaSenha.getText());
                    new Alert(Alert.AlertType.INFORMATION, "Senha alterada com sucesso.").showAndWait();
                } catch (DomainException ex) {
                    new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
                }
            }
        });

        HBox controles = new HBox(10, txtNovaSenha, btnMudarSenha);
        layout.getChildren().addAll(new Label("Usuários (Logins):"), tabela, controles);
        aba.setContent(layout);
        return aba;
    }

    public Scene getScene() { return this.scene; }

    private void fazerLogout() {
        LoginView loginView = new LoginView(stage, usuarioService);
        stage.setScene(loginView.getScene());
    }
}