package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Campanha;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.Foto;
import com.pucpr.projeto.domain.valueObjects.ValorFinanceiro;
import com.pucpr.projeto.enums.StatusCampanha;
import com.pucpr.projeto.services.CampanhaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CampanhaView {

    private final CampanhaService campanhaService;
    private final Stage stage;
    private final Usuario usuarioLogado;
    private final Osc perfilOsc;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private TableView<Campanha> tabela;
    private ObservableList<Campanha> campanhasObservable;

    private TextField txtId, txtTitulo, txtConteudo, txtMeta, txtDataEncerramento;
    private Button btnSalvar, btnExcluir, btnLimpar;

    public CampanhaView(CampanhaService campanhaService, Stage stage, Usuario usuarioLogado, Osc perfilOsc) {
        this.campanhaService = campanhaService;
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.perfilOsc = perfilOsc;
    }

    public Scene getScene(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Button btnVoltar = new Button("⬅ Voltar ao Painel");
        btnVoltar.setOnAction(e -> voltarParaHome());

        Label lblTituloTela = new Label("Gerenciamento de Campanhas");
        lblTituloTela.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox topoLayout = new HBox(15, btnVoltar, lblTituloTela);
        topoLayout.setStyle("-fx-alignment: center-left;");

        tabela = new TableView<>();
        configurarColunasTabela();
        atualizarTabela();

        VBox formLayout = criarFormulario();

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherFormulario(newSelection);
            }
        });

        root.getChildren().addAll(topoLayout, tabela, new Separator(), formLayout);
        return new Scene(root, 700, 600);
    }

    private void voltarParaHome() {
        HomeOscView home = new HomeOscView(stage, usuarioLogado, perfilOsc);
        stage.setScene(home.getScene());
    }

    private void configurarColunasTabela() {
        TableColumn<Campanha, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colTitulo.setPrefWidth(150);

        TableColumn<Campanha, String> colMeta = new TableColumn<>("Meta");
        colMeta.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("R$ %.2f", data.getValue().getMeta().getValor())
        ));

        TableColumn<Campanha, String> colData = new TableColumn<>("Encerramento");
        colData.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataEnce().format(formatter)
        ));

        TableColumn<Campanha, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));

        tabela.getColumns().addAll(colTitulo, colMeta, colData, colStatus);
    }

    private VBox criarFormulario() {
        txtId = new TextField();
        txtId.setPromptText("ID (Gerado automaticamente)");
        txtId.setDisable(true);

        txtTitulo = new TextField();
        txtTitulo.setPromptText("Título da Campanha");

        txtConteudo = new TextField();
        txtConteudo.setPromptText("Descrição / Conteúdo");

        txtMeta = new TextField();
        txtMeta.setPromptText("Meta Financeira (Ex: 1500.50)");

        txtMeta.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtMeta.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        txtDataEncerramento = new TextField();
        txtDataEncerramento.setPromptText("Data Encerramento (DD/MM/AAAA)");

        btnSalvar = new Button("Salvar / Atualizar");
        btnExcluir = new Button("Excluir Selecionado");
        btnLimpar = new Button("Limpar Formulário");

        btnSalvar.setOnAction(e -> salvarOuAtualizar());
        btnExcluir.setOnAction(e -> excluirSelecionado());
        btnLimpar.setOnAction(e -> limparFormulario());

        HBox botoesLayout = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        return new VBox(10,
                new Label("Dados da Campanha:"), txtId, txtTitulo, txtConteudo, txtMeta, txtDataEncerramento, botoesLayout
        );
    }

    private void salvarOuAtualizar() {
        try {
            LocalDate dataEncerramento = LocalDate.parse(txtDataEncerramento.getText(), formatter);
            ValorFinanceiro meta = new ValorFinanceiro(Double.parseDouble(txtMeta.getText()));

            if (txtId.getText().isEmpty()) {
                Long novoId = System.currentTimeMillis();
                Campanha nova = new Campanha(novoId, txtTitulo.getText(), LocalDate.now(), new Foto("url_padrao.jpg"),
                        txtConteudo.getText(), meta, StatusCampanha.ATIVA, dataEncerramento);
                campanhaService.cadastrarCampanha(nova);
            } else {
                Campanha existente = tabela.getSelectionModel().getSelectedItem();
                existente.alterarTitulo(txtTitulo.getText());
                existente.editarConteudo(txtConteudo.getText());
                existente.alterarDataEncerramento(dataEncerramento);
                campanhaService.cadastrarCampanha(existente);
            }

            atualizarTabela();
            limparFormulario();

        } catch (DateTimeParseException ex) {
            mostrarAlerta("Erro de Data", "A data deve estar no formato DD/MM/AAAA.");
        } catch (NumberFormatException ex) {
            mostrarAlerta("Erro de Valor", "Verifique se a meta financeira é um número válido.");
        } catch (Exception ex) {
            mostrarAlerta("Erro", ex.getMessage());
        }
    }

    private void excluirSelecionado() {
        Campanha selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            campanhaService.deletarCampanha(selecionada.getId());
            atualizarTabela();
            limparFormulario();
        } else {
            mostrarAlerta("Aviso", "Selecione uma campanha na tabela para excluir.");
        }
    }

    private void preencherFormulario(Campanha c) {
        txtId.setText(String.valueOf(c.getId()));
        txtTitulo.setText(c.getTitulo());
        txtConteudo.setText(c.getConteudo());
        txtMeta.setText(String.valueOf(c.getMeta().getValor()));
        txtDataEncerramento.setText(c.getDataEnce().format(formatter));
    }

    private void limparFormulario() {
        txtId.clear();
        txtTitulo.clear();
        txtConteudo.clear();
        txtMeta.clear();
        txtDataEncerramento.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void atualizarTabela() {
        campanhasObservable = FXCollections.observableArrayList(campanhaService.listarTodas());
        tabela.setItems(campanhasObservable);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}