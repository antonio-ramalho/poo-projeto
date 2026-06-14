package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.services.PessoaJuridicaService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EdicaoOscAdmView {
    private final Stage stage;
    private final Osc osc;
    private final PessoaJuridicaService service;
    private final Usuario adminLogado;
    private Scene scene;

    private TextField txtNomeLegal, txtNomeComercial;

    public EdicaoOscAdmView(Stage stage, Osc osc, PessoaJuridicaService service, Usuario adminLogado) {
        this.stage = stage;
        this.osc = osc;
        this.service = service;
        this.adminLogado = adminLogado;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titulo = new Label("Editando Instituição: " + osc.getNomeComercial());
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        txtNomeLegal = new TextField(osc.getNomeLegal());
        txtNomeComercial = new TextField(osc.getNomeComercial());

        grid.add(new Label("Nome Legal:"), 0, 0); grid.add(txtNomeLegal, 1, 0);
        grid.add(new Label("Nome Comercial:"), 0, 1); grid.add(txtNomeComercial, 1, 1);

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarEdicao());

        Button btnCancelar = new Button("Cancelar e Voltar");
        btnCancelar.setOnAction(e -> voltarParaHome());

        HBox botoes = new HBox(15, btnSalvar, btnCancelar);

        layout.getChildren().addAll(titulo, new Separator(), grid, botoes);
        this.scene = new Scene(layout, 400, 250);
    }

    private void salvarEdicao() {
        try {
            osc.alterarNomeLegal(txtNomeLegal.getText());
            osc.alterarNomeMarca(txtNomeComercial.getText());

            service.atualizarOsc(osc);

            new Alert(Alert.AlertType.INFORMATION, "OSC atualizada com sucesso!").showAndWait();
            voltarParaHome();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        }
    }

    private void voltarParaHome() {
        HomeAdmView home = new HomeAdmView(stage, adminLogado);
        stage.setScene(home.getScene());
    }

    public Scene getScene() { return this.scene; }
}
