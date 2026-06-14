package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.services.PessoaJuridicaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConsultaOscView {
    private final Stage stage;
    private final PessoaJuridicaService service;
    private Scene scene;

    private TableView<Osc> tabela;
    private TextField txtEditNomeLegal, txtEditNomeComercial;
    private Button btnAtualizar, btnExcluir;

    public ConsultaOscView(Stage stage, PessoaJuridicaService service) {
        this.stage = stage;
        this.service = service;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layoutPrincipal = new VBox(15);
        layoutPrincipal.setPadding(new Insets(20));

        // 1. Configurando a TableView exigida no checklist
        tabela = new TableView<>();

        TableColumn<Osc, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCnpj().getNumeroCnpj()));

        TableColumn<Osc, String> colNomeLegal = new TableColumn<>("Nome Legal");
        colNomeLegal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomeLegal()));

        TableColumn<Osc, String> colNomeComercial = new TableColumn<>("Nome Comercial");
        colNomeComercial.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomeComercial()));

        TableColumn<Osc, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtuacao().name()));

        tabela.getColumns().addAll(colCnpj, colNomeLegal, colNomeComercial, colCategoria);
        tabela.setPrefHeight(200);

        // 2. Formulário de Edição (Aparece ao selecionar um registro)
        GridPane gridEdicao = new GridPane();
        gridEdicao.setHgap(10);
        gridEdicao.setVgap(10);

        txtEditNomeLegal = new TextField();
        txtEditNomeComercial = new TextField();

        btnAtualizar = new Button("Salvar Alterações (U)");
        btnExcluir = new Button("Excluir OSC (D)");
        btnAtualizar.setDisable(true);
        btnExcluir.setDisable(true);

        gridEdicao.add(new Label("Editar Nome Legal:"), 0, 0); gridEdicao.add(txtEditNomeLegal, 1, 0);
        gridEdicao.add(new Label("Editar Nome Comercial:"), 0, 1); gridEdicao.add(txtEditNomeComercial, 1, 1);

        HBox botoesAcao = new HBox(10, btnAtualizar, btnExcluir);
        gridEdicao.add(botoesAcao, 1, 2);

        // 3. Regra de Negócio Visual: Preencher o form ao clicar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtEditNomeLegal.setText(newSelection.getNomeLegal());
                txtEditNomeComercial.setText(newSelection.getNomeComercial());
                btnAtualizar.setDisable(false);
                btnExcluir.setDisable(false);
            } else {
                txtEditNomeLegal.clear();
                txtEditNomeComercial.clear();
                btnAtualizar.setDisable(true);
                btnExcluir.setDisable(true);
            }
        });

        // 4. Ações dos Botões
        btnAtualizar.setOnAction(e -> atualizarRegistro());
        btnExcluir.setOnAction(e -> excluirRegistro());

        Button btnVoltar = new Button("Voltar para Home");
        btnVoltar.setOnAction(e -> voltarParaHome());

        layoutPrincipal.getChildren().addAll(new Label("Consulta de ONGs Cadastradas"), tabela, new Separator(), gridEdicao, btnVoltar);
        this.scene = new Scene(layoutPrincipal, 600, 500);

        carregarDados();
    }

    private void carregarDados() {
        tabela.getItems().setAll(service.buscarTodas());
    }

    private void atualizarRegistro() {
        Osc oscSelecionada = tabela.getSelectionModel().getSelectedItem();
        if (oscSelecionada != null) {
            try {
                oscSelecionada.alterarNomeLegal(txtEditNomeLegal.getText());
                oscSelecionada.alterarNomeMarca(txtEditNomeComercial.getText());

                service.atualizarOsc(oscSelecionada);
                new Alert(Alert.AlertType.INFORMATION, "OSC atualizada com sucesso!").showAndWait();
                carregarDados();

            } catch (DomainException ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
            }
        }
    }

    private void excluirRegistro() {
        Osc oscSelecionada = tabela.getSelectionModel().getSelectedItem();
        if (oscSelecionada != null) {
            try {
                service.excluirOsc(oscSelecionada.getId());
                new Alert(Alert.AlertType.INFORMATION, "OSC excluída com sucesso!").showAndWait();
                carregarDados();
            } catch (DomainException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        }
    }

    private void voltarParaHome() {
        com.pucpr.projeto.repositories.UsuarioRepository repo = new com.pucpr.projeto.repositories.UsuarioRepository();
        com.pucpr.projeto.services.UsuarioService usuarioService = new com.pucpr.projeto.services.UsuarioService(repo);
        LoginView loginView = new LoginView(stage, usuarioService);
        stage.setScene(loginView.getScene());
    }

    public Scene getScene() { return this.scene; }
}
