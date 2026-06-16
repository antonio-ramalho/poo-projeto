package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Despesa;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.DespesaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeOscView {
    private final Stage stage;
    private final Usuario usuarioLogado;
    private final Osc perfilOsc;
    private final DespesaService despesaService;
    private Scene scene;
    private TableView<Despesa> tabelaDespesas;

    public HomeOscView(Stage stage, Usuario usuarioLogado, Osc perfilOsc, DespesaService despesaService) {
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.perfilOsc = perfilOsc;
        this.despesaService = despesaService;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label lblBoasVindas = new Label("Painel da Instituição: " + perfilOsc.getNomeComercial());
        lblBoasVindas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Tabelas de Campanhas (do seu amigo)
        Label lblMinhasCampanhas = new Label("Nossas Campanhas Ativas:");
        TableView<?> tabelaCampanhas = new TableView<>();
        tabelaCampanhas.setPlaceholder(new Label("Suas campanhas aparecerão aqui..."));

        // Tabela de Despesas (nossa nova funcionalidade)
        Label lblPrestacaoContas = new Label("Registro de Despesas (Transparência):");
        tabelaDespesas = new TableView<>();

        TableColumn<Despesa, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescricao()));

        TableColumn<Despesa, String> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValor().toString()));

        TableColumn<Despesa, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));

        tabelaDespesas.getColumns().addAll(colDesc, colValor, colData);
        carregarDespesas();

        Button btnCadastrarDespesa = new Button("Cadastrar Nova Despesa");
        btnCadastrarDespesa.setOnAction(e -> {
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            CadastroDespesaView telaCadastro = new CadastroDespesaView(modalStage, despesaService, perfilOsc);
            modalStage.setScene(telaCadastro.getScene());
            modalStage.setOnHidden(evt -> carregarDespesas());
            modalStage.showAndWait();
        });

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setOnAction(e -> fazerLogout());

        layout.getChildren().addAll(lblBoasVindas, new Separator(),
                lblMinhasCampanhas, tabelaCampanhas,
                lblPrestacaoContas, btnCadastrarDespesa, tabelaDespesas, btnSair);
        this.scene = new Scene(layout, 600, 600);
    }

    private void carregarDespesas() {
        tabelaDespesas.getItems().setAll(despesaService.buscarPorOsc(perfilOsc.getId()));
    }

    public Scene getScene() { return this.scene; }

    private void fazerLogout() {
        UsuarioService uService = new UsuarioService(new UsuarioRepository());
        LoginView loginView = new LoginView(stage, uService);
        stage.setScene(loginView.getScene());
    }
}