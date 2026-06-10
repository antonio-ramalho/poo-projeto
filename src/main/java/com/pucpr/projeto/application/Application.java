package com.pucpr.projeto.application;

import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.UsuarioService;
import com.pucpr.projeto.views.LoginView;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {
        UsuarioRepository repo = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(repo);

        LoginView loginView = new LoginView(primaryStage, usuarioService);

        primaryStage.setTitle("Sistema Coopafi");
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
