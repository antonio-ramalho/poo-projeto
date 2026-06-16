package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.ValorMonetario;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.services.DoacaoService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CadastroDoacaoView {
    private final Stage stage;
    private final DoacaoService service;
    private final Usuario usuarioLogado;
    private final Osc oscAlvo;
    private Scene scene;

    private DatePicker datePickerData;
    private TextField txtValor;
    private TextField txtMensagem;

    public CadastroDoacaoView(Stage stage, DoacaoService service, Usuario usuarioLogado, Osc oscAlvo) {
        this.stage = stage;
        this.service = service;
        this.usuarioLogado = usuarioLogado;
        this.oscAlvo = oscAlvo;
        inicializarTela();
    }

    private void inicializarTela() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(12);
        grid.setHgap(15);

        Label lblInfo = new Label("Doando para: " + oscAlvo.getNomeComercial());
        lblInfo.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");

        datePickerData = new DatePicker(LocalDate.now());
        txtValor = new TextField();
        txtValor.setPromptText("Ex: 50.00");
        txtMensagem = new TextField();
        txtMensagem.setPromptText("Mensagem de apoio (opcional)");

        grid.add(lblInfo, 0, 0, 2, 1);
        grid.add(new Label("Data da Doação:"), 0, 1); grid.add(datePickerData, 1, 1);
        grid.add(new Label("Valor (R$):"), 0, 2);     grid.add(txtValor, 1, 2);
        grid.add(new Label("Mensagem:"), 0, 3);       grid.add(txtMensagem, 1, 3);

        Button btnSalvar = new Button("Salvar Doação");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnVoltar = new Button("Voltar");

        btnSalvar.setOnAction(e -> executarCadastro());
        btnVoltar.setOnAction(e -> voltar());

        HBox botoes = new HBox(15, btnSalvar, btnVoltar);
        grid.add(botoes, 1, 5);

        this.scene = new Scene(grid, 450, 300);
    }

    public Scene getScene() {
        return this.scene;
    }

    private void executarCadastro() {
        try {
            LocalDate data = datePickerData.getValue();
            if (data == null) throw new DomainException("A data da doação é obrigatória.");

            String valorTexto = txtValor.getText().replace(",", ".");
            BigDecimal valorBd = new BigDecimal(valorTexto);
            ValorMonetario valorMonetario = new ValorMonetario(valorBd);

            // Passando o ID da OSC e o ID do usuário (doador) corretamente
            service.cadastrar(data, valorMonetario, txtMensagem.getText(), oscAlvo.getId(), usuarioLogado.getId());

            new Alert(Alert.AlertType.INFORMATION, "Doação registrada com sucesso para " + oscAlvo.getNomeComercial() + "!").showAndWait();
            voltar();

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.WARNING, "Digite um valor numérico válido. Ex: 50.00").showAndWait();
        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Erro inesperado: " + ex.getMessage()).showAndWait();
        }
    }

    private void voltar() {
        HomeDoadorView home = new HomeDoadorView(stage, usuarioLogado);
        stage.setScene(home.getScene());
    }
}