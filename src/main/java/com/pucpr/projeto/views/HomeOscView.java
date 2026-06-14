package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.UsuarioService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
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

        Label lblMinhasCampanhas = new Label("Nossas Campanhas Ativas:");
        TableView<?> tabelaCampanhas = new TableView<>();
        tabelaCampanhas.setPlaceholder(new Label("Suas campanhas aparecerão aqui..."));

        Label lblPrestacaoContas = new Label("Registro de Despesas (Transparência):");
        TableView<?> tabelaDespesas = new TableView<>();
        tabelaDespesas.setPlaceholder(new Label("Suas despesas aparecerão aqui..."));

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setOnAction(e -> fazerLogout());

        layout.getChildren().addAll(lblBoasVindas, new Separator(), lblMinhasCampanhas, tabelaCampanhas, lblPrestacaoContas, tabelaDespesas, btnSair);
        this.scene = new Scene(layout, 600, 550);
    }

    public Scene getScene() { return this.scene; }

    private void fazerLogout() {
        UsuarioService uService = new UsuarioService(new UsuarioRepository());
        LoginView loginView = new LoginView(stage, uService);
        stage.setScene(loginView.getScene());
    }
}
