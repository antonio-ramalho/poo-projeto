package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.valueObjects.ValorMonetario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.services.DespesaService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.math.BigDecimal; // Importante: Importar BigDecimal
import java.time.LocalDate;

public class CadastroDespesaView {
    private final Stage stage;
    private final DespesaService despesaService;
    private final Osc osc;
    private Scene scene;

    public CadastroDespesaView(Stage stage, DespesaService despesaService, Osc osc) {
        this.stage = stage;
        this.despesaService = despesaService;
        this.osc = osc;
        inicializarTela();
    }

    private void inicializarTela() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição da despesa");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor (ex: 150.00)");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        Button btnSalvar = new Button("Salvar Despesa");
        btnSalvar.setOnAction(e -> {
            try {
                // Correção: Converter para double e depois para BigDecimal
                double valorDouble = Double.parseDouble(txtValor.getText());
                ValorMonetario valor = new ValorMonetario(BigDecimal.valueOf(valorDouble));

                despesaService.cadastrar(datePicker.getValue(), valor, txtDescricao.getText(), osc.getId());

                new Alert(Alert.AlertType.INFORMATION, "Despesa cadastrada!").showAndWait();
                stage.close();
            } catch (DomainException | NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
            }
        });

        grid.add(new Label("Descrição:"), 0, 0);
        grid.add(txtDescricao, 1, 0);
        grid.add(new Label("Valor:"), 0, 1);
        grid.add(txtValor, 1, 1);
        grid.add(new Label("Data:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(btnSalvar, 1, 3);

        this.scene = new Scene(grid, 300, 200);
    }

    public Scene getScene() {
        return this.scene;
    }
}