package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.DocOsc;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.Cep;
import com.pucpr.projeto.domain.valueObjects.Email;
import com.pucpr.projeto.domain.valueObjects.Endereco;
import com.pucpr.projeto.domain.valueObjects.Telefone;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.TipoDoc;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.CampanhaRepository;
import com.pucpr.projeto.repositories.OscRepository;
import com.pucpr.projeto.repositories.PostagemRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.CampanhaService;
import com.pucpr.projeto.services.PessoaJuridicaService;
import com.pucpr.projeto.services.PostagemService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

public class HomeOscView {
    private final Stage stage;
    private final Usuario usuarioLogado;
    private Osc perfilOsc;
    private Scene scene;

    private final PessoaJuridicaService pjService;
    private final UsuarioService usuarioService;

    private TextField txtNomeLegal, txtNomeComercial, txtEmail, txtTelefone, txtChavePix;
    private TextField txtCep, txtRua, txtBairro, txtCidade, txtNumero;
    private ComboBox<String> comboAtuacao;

    private TableView<DocOsc> tabelaDocs;
    private TextField txtDataEmissaoDoc, txtUrlArquivo;
    private ComboBox<String> comboTipoDoc;

    public HomeOscView(Stage stage, Usuario usuarioLogado, Osc perfilOsc) {
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.perfilOsc = perfilOsc;

        this.usuarioService = new UsuarioService(new UsuarioRepository());
        this.pjService = new PessoaJuridicaService(new UsuarioRepository(), new OscRepository());

        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        // 1. Cabeçalho
        HBox cabecalho = new HBox(20);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        Label lblBoasVindas = new Label("Painel da Instituição: " + perfilOsc.getNomeComercial());
        lblBoasVindas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setOnAction(e -> fazerLogout());
        cabecalho.getChildren().addAll(lblBoasVindas, btnSair);

        // 2. Integração dos seus botões (Módulo 3 - Lucas)
        Button btnGerenciarCampanhas = new Button("Gerenciar Campanhas");
        btnGerenciarCampanhas.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGerenciarCampanhas.setOnAction(e -> abrirTelaCampanhas());

        Button btnMuralComunidade = new Button("Mural da Comunidade");
        btnMuralComunidade.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        btnMuralComunidade.setOnAction(e -> abrirTelaPostagens());

        HBox botoesNavegacao = new HBox(10, btnGerenciarCampanhas, btnMuralComunidade);
        botoesNavegacao.setPadding(new Insets(5, 0, 5, 0));

        // 3. Abas do Felipe (Módulo 2)
        TabPane painelAbas = new TabPane();
        painelAbas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        painelAbas.getTabs().add(criarAbaPerfil());
        painelAbas.getTabs().add(criarAbaDocumentos());

        // Montando a tela final
        layout.getChildren().addAll(cabecalho, botoesNavegacao, new Separator(), painelAbas);
        this.scene = new Scene(layout, 800, 650);
    }

    // --- MÉTODOS DE NAVEGAÇÃO DO MÓDULO 3 (LUCAS) ---
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
    // ------------------------------------------------

    private Tab criarAbaPerfil() {
        Tab aba = new Tab("Meu Perfil Institucional");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(12);
        grid.setHgap(15);

        txtNomeLegal = new TextField(perfilOsc.getNomeLegal());
        txtNomeComercial = new TextField(perfilOsc.getNomeComercial());
        txtEmail = new TextField(perfilOsc.getEmail().getEnderecoEmail());
        txtTelefone = new TextField(perfilOsc.getTelefone().getNumeroTelefone());
        txtChavePix = new TextField(perfilOsc.getChavePix());

        comboAtuacao = new ComboBox<>();
        Arrays.stream(Categoria.values()).forEach(c -> comboAtuacao.getItems().add(c.name()));
        comboAtuacao.setValue(perfilOsc.getAtuacao() != null ? perfilOsc.getAtuacao().name() : null);

        txtCep = new TextField(perfilOsc.getEndereco().getNumeroCep().getNumeroCep());
        txtRua = new TextField(perfilOsc.getEndereco().getRua());
        txtBairro = new TextField(perfilOsc.getEndereco().getBairro());
        txtCidade = new TextField(perfilOsc.getEndereco().getCidade());
        txtNumero = new TextField(perfilOsc.getEndereco().getNumeroEndereco());

        grid.add(new Label("Nome Legal:"), 0, 0);       grid.add(txtNomeLegal, 1, 0);
        grid.add(new Label("Nome Comercial:"), 0, 1);   grid.add(txtNomeComercial, 1, 1);
        grid.add(new Label("E-mail:"), 0, 2);           grid.add(txtEmail, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3);         grid.add(txtTelefone, 1, 3);
        grid.add(new Label("Chave PIX:"), 0, 4);        grid.add(txtChavePix, 1, 4);
        grid.add(new Label("Categoria:"), 0, 5);        grid.add(comboAtuacao, 1, 5);

        grid.add(new Label("CEP:"), 2, 0);              grid.add(txtCep, 3, 0);
        grid.add(new Label("Cidade:"), 2, 1);           grid.add(txtCidade, 3, 1);
        grid.add(new Label("Rua/Av:"), 2, 2);           grid.add(txtRua, 3, 2);
        grid.add(new Label("Bairro:"), 2, 3);           grid.add(txtBairro, 3, 3);
        grid.add(new Label("Número:"), 2, 4);           grid.add(txtNumero, 3, 4);

        Button btnAtualizar = new Button("Salvar Alterações");
        btnAtualizar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAtualizar.setOnAction(e -> atualizarDadosOsc());

        Button btnExcluir = new Button("Excluir Instituição");
        btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnExcluir.setOnAction(e -> excluirContaOsc());

        HBox botoes = new HBox(15, btnAtualizar, btnExcluir);
        grid.add(botoes, 0, 7, 4, 1);

        aba.setContent(grid);
        return aba;
    }

    private void atualizarDadosOsc() {
        try {
            perfilOsc.alterarNomeLegal(txtNomeLegal.getText());
            perfilOsc.alterarNomeMarca(txtNomeComercial.getText());
            perfilOsc.alterarChavePix(txtChavePix.getText());
            perfilOsc.alterarCategoria(Categoria.valueOf(comboAtuacao.getValue()));

            if (!txtEmail.getText().isEmpty()) perfilOsc.atualizarEmail(new Email(txtEmail.getText()));
            if (!txtTelefone.getText().isEmpty()) perfilOsc.atualizarTelefone(new Telefone(txtTelefone.getText()));
            if (!txtCep.getText().isEmpty() && !txtRua.getText().isEmpty()) {
                perfilOsc.atualizarEndereco(new Endereco(new Cep(txtCep.getText()), txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText()));
            }

            pjService.atualizarOsc(perfilOsc);
            new Alert(Alert.AlertType.INFORMATION, "Dados da OSC atualizados com sucesso!").showAndWait();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Erro ao atualizar dados: " + ex.getMessage()).showAndWait();
        }
    }

    private void excluirContaOsc() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Excluir OSC");
        confirmacao.setHeaderText("Tem certeza que deseja excluir sua instituição?");
        confirmacao.setContentText("Esta ação é irreversível.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                pjService.excluirOsc(perfilOsc.getId());
                usuarioService.excluir(usuarioLogado.getId());
                new Alert(Alert.AlertType.INFORMATION, "Instituição excluída com sucesso.").showAndWait();
                fazerLogout();
            } catch (DomainException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        }
    }

    private Tab criarAbaDocumentos() {
        Tab aba = new Tab("Documentos Comprobatórios");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(15));

        Label lblTrust = new Label("TrustScore Atual: " + perfilOsc.getTrustScore());
        lblTrust.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2;");

        tabelaDocs = new TableView<>();

        TableColumn<DocOsc, String> colTipo = new TableColumn<>("Tipo de Documento");
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo().getDescricao()));

        TableColumn<DocOsc, String> colData = new TableColumn<>("Data Emissão");
        colData.setCellValueFactory(data -> {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new SimpleStringProperty(data.getValue().getDataEmissao().format(fmt));
        });

        TableColumn<DocOsc, String> colUrl = new TableColumn<>("URL / Caminho");
        colUrl.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUrlArquivo()));

        TableColumn<DocOsc, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));

        tabelaDocs.getColumns().addAll(colTipo, colData, colUrl, colStatus);
        atualizarTabelaDocs();

        GridPane gridForm = new GridPane();
        gridForm.setHgap(10); gridForm.setVgap(10);

        comboTipoDoc = new ComboBox<>();
        Arrays.stream(TipoDoc.values()).forEach(t -> comboTipoDoc.getItems().add(t.name()));

        txtDataEmissaoDoc = new TextField();
        txtDataEmissaoDoc.setPromptText("DD/MM/AAAA");

        txtUrlArquivo = new TextField();
        txtUrlArquivo.setPromptText("Ex: https://drive.google.com/...");

        gridForm.add(new Label("Tipo de Documento:"), 0, 0); gridForm.add(comboTipoDoc, 1, 0);
        gridForm.add(new Label("Data Emissão:"), 0, 1);      gridForm.add(txtDataEmissaoDoc, 1, 1);
        gridForm.add(new Label("URL do Arquivo:"), 0, 2);    gridForm.add(txtUrlArquivo, 1, 2);

        Button btnAdicionar = new Button("Adicionar");
        Button btnAtualizarDoc = new Button("Atualizar Selecionado");
        Button btnExcluirDoc = new Button("Excluir Selecionado");

        Button btnMockAprovar = new Button("Simular Validação");
        btnMockAprovar.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");

        btnAtualizarDoc.setDisable(true);
        btnExcluirDoc.setDisable(true);
        btnMockAprovar.setDisable(true);

        tabelaDocs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                comboTipoDoc.setValue(newSelection.getTipo().name());
                txtDataEmissaoDoc.setText(newSelection.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                txtUrlArquivo.setText(newSelection.getUrlArquivo());

                btnAtualizarDoc.setDisable(false);
                btnExcluirDoc.setDisable(false);
                btnMockAprovar.setDisable(false);
                btnAdicionar.setDisable(true);
            } else {
                limparFormDoc();
                btnAtualizarDoc.setDisable(true);
                btnExcluirDoc.setDisable(true);
                btnMockAprovar.setDisable(true);
                btnAdicionar.setDisable(false);
            }
        });

        btnAdicionar.setOnAction(e -> adicionarDocumento());
        btnAtualizarDoc.setOnAction(e -> atualizarDocumento());
        btnExcluirDoc.setOnAction(e -> excluirDocumento());
        btnMockAprovar.setOnAction(e -> simularValidacaoMock(lblTrust));

        Button btnLimparSelecao = new Button("Limpar Seleção");
        btnLimparSelecao.setOnAction(e -> tabelaDocs.getSelectionModel().clearSelection());

        HBox botoesDoc = new HBox(10, btnAdicionar, btnAtualizarDoc, btnExcluirDoc, btnMockAprovar, btnLimparSelecao);

        layout.getChildren().addAll(lblTrust, new Label("Gerenciamento de Documentos da Instituição:"), tabelaDocs, new Separator(), gridForm, botoesDoc);
        aba.setContent(layout);
        return aba;
    }

    private void atualizarTabelaDocs() {
        tabelaDocs.getItems().setAll(perfilOsc.getDocumentos());
    }

    private void limparFormDoc() {
        comboTipoDoc.setValue(null);
        txtDataEmissaoDoc.clear();
        txtUrlArquivo.clear();
    }

    private void adicionarDocumento() {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(txtDataEmissaoDoc.getText(), fmt);
            TipoDoc tipo = TipoDoc.valueOf(comboTipoDoc.getValue());

            DocOsc novoDoc = new DocOsc(data, tipo, txtUrlArquivo.getText());
            perfilOsc.addDocumento(novoDoc);

            pjService.atualizarOsc(perfilOsc);
            atualizarTabelaDocs();
            limparFormDoc();
            new Alert(Alert.AlertType.INFORMATION, "Documento adicionado!").showAndWait();
        } catch (DateTimeParseException ex) {
            new Alert(Alert.AlertType.WARNING, "Data inválida! Use DD/MM/AAAA.").showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, "Preencha todos os campos corretamente.").showAndWait();
        }
    }

    private void atualizarDocumento() {
        DocOsc docSelecionado = tabelaDocs.getSelectionModel().getSelectedItem();
        if (docSelecionado != null) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(txtDataEmissaoDoc.getText(), fmt);
                TipoDoc tipo = TipoDoc.valueOf(comboTipoDoc.getValue());

                docSelecionado.alterarDataEmissao(data);
                docSelecionado.alterarTipo(tipo);
                docSelecionado.alterarUrlArquivo(txtUrlArquivo.getText());

                pjService.atualizarOsc(perfilOsc);
                atualizarTabelaDocs();
                tabelaDocs.getSelectionModel().clearSelection();
                new Alert(Alert.AlertType.INFORMATION, "Documento atualizado!").showAndWait();
            } catch (DateTimeParseException ex) {
                new Alert(Alert.AlertType.WARNING, "Data inválida! Use DD/MM/AAAA.").showAndWait();
            }
        }
    }

    private void excluirDocumento() {
        DocOsc docSelecionado = tabelaDocs.getSelectionModel().getSelectedItem();
        if (docSelecionado != null) {
            perfilOsc.getDocumentos().remove(docSelecionado);
            pjService.atualizarOsc(perfilOsc);
            atualizarTabelaDocs();
            new Alert(Alert.AlertType.INFORMATION, "Documento removido!").showAndWait();
        }
    }

    private void simularValidacaoMock(Label lblTrust) {
        DocOsc docSelecionado = tabelaDocs.getSelectionModel().getSelectedItem();
        if (docSelecionado != null) {
            try {
                pjService.validarDocumentoMock(perfilOsc.getId(), docSelecionado.getId(), true);
                perfilOsc = pjService.buscarPorId(perfilOsc.getId());
                lblTrust.setText("TrustScore Atual: " + perfilOsc.getTrustScore());
                atualizarTabelaDocs();
                new Alert(Alert.AlertType.INFORMATION, "Validação simulada! Documento Aprovado e TrustScore atualizado.").showAndWait();
            } catch (DomainException ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
            }
        }
    }

    public Scene getScene() { return this.scene; }

    private void fazerLogout() {
        LoginView loginView = new LoginView(stage, usuarioService);
        stage.setScene(loginView.getScene());
    }
}