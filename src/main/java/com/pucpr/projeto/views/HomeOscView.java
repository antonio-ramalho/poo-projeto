package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Campanha;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.repositories.CampanhaRepository;
import com.pucpr.projeto.repositories.PostagemRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.CampanhaService;
import com.pucpr.projeto.services.PostagemService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeOscView {
    private final Stage stage;
    private final Usuario usuarioLogado;
    private final Osc perfilOsc;
    private Scene scene;

    public HomeOscView(Stage stage, Usuario usuarioLogado, Osc perfilOsc) {
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.perfilOsc = perfilOsc;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label lblBoasVindas = new Label("Painel da Instituição: " + perfilOsc.getNomeComercial());
        lblBoasVindas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");


        Button btnGerenciarCampanhas = new Button("Gerenciar Campanhas");
        btnGerenciarCampanhas.setOnAction(e -> abrirTelaCampanhas());

        Button btnMuralComunidade = new Button("Mural da Comunidade");
        btnMuralComunidade.setOnAction(e -> abrirTelaPostagens());

        HBox botoesNavegacao = new HBox(10, btnGerenciarCampanhas, btnMuralComunidade);


        Label lblMinhasCampanhas = new Label("Nossas Campanhas Ativas:");


        TableView<Campanha> tabelaCampanhas = new TableView<>();
        tabelaCampanhas.setPlaceholder(new Label("Nenhuma campanha encontrada..."));

        TableColumn<Campanha, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colTitulo.setPrefWidth(200);

        TableColumn<Campanha, String> colMeta = new TableColumn<>("Meta");
        colMeta.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("R$ %.2f", data.getValue().getMeta().getValor())
        ));
        colMeta.setPrefWidth(150);

        TableColumn<Campanha, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));
        colStatus.setPrefWidth(100);

        tabelaCampanhas.getColumns().addAll(colTitulo, colMeta, colStatus);


        CampanhaService campanhaService = new CampanhaService(new CampanhaRepository());
        ObservableList<Campanha> campanhasObservable = FXCollections.observableArrayList(campanhaService.listarTodas());
        tabelaCampanhas.setItems(campanhasObservable);


        Label lblPrestacaoContas = new Label("Registro de Despesas (Transparência):");
        TableView<?> tabelaDespesas = new TableView<>();
        tabelaDespesas.setPlaceholder(new Label("Suas despesas aparecerão aqui..."));

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setOnAction(e -> fazerLogout());

        layout.getChildren().addAll(
                lblBoasVindas,
                botoesNavegacao,
                new Separator(),
                lblMinhasCampanhas,
                tabelaCampanhas,
                lblPrestacaoContas,
                tabelaDespesas,
                btnSair
        );
        this.scene = new Scene(layout, 600, 650);
    }

    public Scene getScene() { return this.scene; }

    private void abrirTelaCampanhas() {
        CampanhaRepository repo = new CampanhaRepository();
        CampanhaService service = new CampanhaService(repo);
        CampanhaView view = new CampanhaView(service, stage, usuarioLogado, perfilOsc);
        stage.setScene(view.getScene(stage));
    }

    private void abrirTelaPostagens() {
        PostagemRepository repo = new PostagemRepository();
        PostagemService service = new PostagemService(repo);
        PostagemView view = new PostagemView(service, stage, usuarioLogado, perfilOsc);
        stage.setScene(view.getScene(stage));
    }

    private void fazerLogout() {
        UsuarioService uService = new UsuarioService(new UsuarioRepository());
        LoginView loginView = new LoginView(stage, uService);
        stage.setScene(loginView.getScene());
    }
}
