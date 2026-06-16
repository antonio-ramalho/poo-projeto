package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Postagem;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.Foto;
import com.pucpr.projeto.services.PostagemService;
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

public class PostagemView {

    private final PostagemService postagemService;
    private final Stage stage;
    private final Usuario usuarioLogado;
    private final Osc perfilOsc;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private TableView<Postagem> tabela;
    private ObservableList<Postagem> postagensObservable;

    private TextField txtId, txtTitulo, txtConteudo, txtDataPublicacao, txtUrlFoto;
    private Button btnSalvar, btnExcluir, btnLimpar;

    public PostagemView(PostagemService postagemService, Stage stage, Usuario usuarioLogado, Osc perfilOsc) {
        this.postagemService = postagemService;
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.perfilOsc = perfilOsc;
    }

    public Scene getScene(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));


        Button btnVoltar = new Button("⬅ Voltar ao Painel");
        btnVoltar.setOnAction(e -> voltarParaHome());

        Label lblTituloTela = new Label("Gerenciamento de Postagens (Comunidade)");
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
        TableColumn<Postagem, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colTitulo.setPrefWidth(200);

        TableColumn<Postagem, String> colData = new TableColumn<>("Data de Publicação");
        colData.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataPub().format(formatter)
        ));
        colData.setPrefWidth(150);

        TableColumn<Postagem, String> colConteudo = new TableColumn<>("Conteúdo");
        colConteudo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConteudo()));
        colConteudo.setPrefWidth(300);

        tabela.getColumns().addAll(colTitulo, colData, colConteudo);
    }

    private VBox criarFormulario() {
        txtId = new TextField();
        txtId.setPromptText("ID (Gerado automaticamente)");
        txtId.setDisable(true);

        txtTitulo = new TextField();
        txtTitulo.setPromptText("Título da Postagem");

        txtConteudo = new TextField();
        txtConteudo.setPromptText("Escreva sua postagem aqui...");

        txtDataPublicacao = new TextField();
        txtDataPublicacao.setPromptText("Data de Publicação (DD/MM/AAAA)");

        txtUrlFoto = new TextField();
        txtUrlFoto.setPromptText("URL da Foto (opcional)");

        btnSalvar = new Button("Salvar / Atualizar");
        btnExcluir = new Button("Excluir Selecionado");
        btnLimpar = new Button("Limpar Formulário");

        btnSalvar.setOnAction(e -> salvarOuAtualizar());
        btnExcluir.setOnAction(e -> excluirSelecionado());
        btnLimpar.setOnAction(e -> limparFormulario());

        HBox botoesLayout = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        return new VBox(10,
                new Label("Criar / Editar Postagem:"),
                txtId, txtTitulo, txtConteudo, txtDataPublicacao, txtUrlFoto, botoesLayout
        );
    }

    private void salvarOuAtualizar() {
        try {
            LocalDate dataPub = LocalDate.parse(txtDataPublicacao.getText(), formatter);
            Foto foto = new Foto(txtUrlFoto.getText().isEmpty() ? "sem_foto.jpg" : txtUrlFoto.getText());

            if (txtId.getText().isEmpty()) {
                Long novoId = System.currentTimeMillis();
                Postagem nova = new Postagem(novoId, txtTitulo.getText(), dataPub, foto, txtConteudo.getText());
                postagemService.criarPostagem(nova);
            } else {
                Postagem existente = tabela.getSelectionModel().getSelectedItem();
                existente.alterarTitulo(txtTitulo.getText());
                existente.editarConteudo(txtConteudo.getText());
                existente.alterarFoto(foto);

            }

            atualizarTabela();
            limparFormulario();

        } catch (DateTimeParseException ex) {
            mostrarAlerta("Erro de Data", "A data deve estar no formato DD/MM/AAAA.");
        } catch (Exception ex) {
            mostrarAlerta("Erro", "Ocorreu um erro ao salvar: " + ex.getMessage());
        }
    }

    private void excluirSelecionado() {
        Postagem selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada != null) {

            atualizarTabela();
            limparFormulario();
        } else {
            mostrarAlerta("Aviso", "Selecione uma postagem na tabela para excluir.");
        }
    }

    private void preencherFormulario(Postagem p) {
        txtId.setText(String.valueOf(p.getId()));
        txtTitulo.setText(p.getTitulo());
        txtConteudo.setText(p.getConteudo());
        txtDataPublicacao.setText(p.getDataPub().format(formatter));
        if (p.getFoto() != null) {
            txtUrlFoto.setText(p.getFoto().getUrl());
        }
    }

    private void limparFormulario() {
        txtId.clear();
        txtTitulo.clear();
        txtConteudo.clear();
        txtDataPublicacao.clear();
        txtUrlFoto.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void atualizarTabela() {
        postagensObservable = FXCollections.observableArrayList(postagemService.listarTodas());
        tabela.setItems(postagensObservable);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}