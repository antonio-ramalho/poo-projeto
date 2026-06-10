package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeView {
    private final Stage stage;
    private final Usuario usuarioLogado;
    private Scene scene;

    public HomeView(Stage stage, Usuario usuarioLogado) {
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label lblBoasVindas = new Label("Bem-vindo ao Sistema Grative!");
        lblBoasVindas.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblUsuario = new Label("Usuário logado: " + usuarioLogado.getLogin());

        layout.getChildren().addAll(lblBoasVindas, lblUsuario);

        this.scene = new Scene(layout, 400, 300);
    }

    public Scene getScene() {
        return this.scene;
    }
}
