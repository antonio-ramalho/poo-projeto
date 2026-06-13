package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.DocOsc;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.TipoDoc;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.PessoaJuridicaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class CadastroOscView {
    private final Stage stage;
    private final PessoaJuridicaService service;
    private Scene scene;

    private TextField txtNomeLegal, txtNomeComercial, txtCnpj, txtDataFundacao, txtChavePix;
    private TextField txtCep, txtCidade, txtRua, txtBairro, txtNumero;
    private TextField txtEmail, txtTelefone, txtLogin;
    private PasswordField txtSenha;
    private ComboBox<String> comboCategoria;

    // Campos do Documento Obrigatório
    private ComboBox<String> comboTipoDoc;
    private TextField txtUrlArquivo, txtDataEmissaoDoc;

    public CadastroOscView(Stage stage, PessoaJuridicaService service) {
        this.stage = stage;
        this.service = service;
        inicializarTela();
    }

    private void inicializarTela() {
        // Exigência: Interface Gráfica em JavaFX puro
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Instanciando campos da OSC
        txtNomeLegal = new TextField();
        txtNomeComercial = new TextField();
        txtCnpj = new TextField();
        txtDataFundacao = new TextField();
        txtDataFundacao.setPromptText("DD/MM/AAAA");
        txtChavePix = new TextField();

        txtCep = new TextField();
        txtCidade = new TextField();
        txtRua = new TextField();
        txtBairro = new TextField();
        txtNumero = new TextField();

        txtEmail = new TextField();
        txtTelefone = new TextField();
        txtLogin = new TextField();
        txtSenha = new PasswordField();

        comboCategoria = new ComboBox<>();
        Arrays.stream(Categoria.values()).forEach(c -> comboCategoria.getItems().add(c.name()));

        // Instanciando campos do Documento
        comboTipoDoc = new ComboBox<>();
        Arrays.stream(TipoDoc.values()).forEach(t -> comboTipoDoc.getItems().add(t.name()));
        txtUrlArquivo = new TextField();
        txtDataEmissaoDoc = new TextField();
        txtDataEmissaoDoc.setPromptText("DD/MM/AAAA");

        // Adicionando ao Layout
        grid.add(new Label("Nome Legal:"), 0, 0); grid.add(txtNomeLegal, 1, 0);
        grid.add(new Label("Nome Comercial:"), 0, 1); grid.add(txtNomeComercial, 1, 1);
        grid.add(new Label("CNPJ:"), 0, 2); grid.add(txtCnpj, 1, 2);
        grid.add(new Label("Data Fundação:"), 0, 3); grid.add(txtDataFundacao, 1, 3);
        grid.add(new Label("Chave PIX:"), 0, 4); grid.add(txtChavePix, 1, 4);
        grid.add(new Label("Categoria:"), 0, 5); grid.add(comboCategoria, 1, 5);

        grid.add(new Label("E-mail:"), 0, 6); grid.add(txtEmail, 1, 6);
        grid.add(new Label("Telefone:"), 0, 7); grid.add(txtTelefone, 1, 7);
        grid.add(new Label("Login:"), 0, 8); grid.add(txtLogin, 1, 8);
        grid.add(new Label("Senha:"), 0, 9); grid.add(txtSenha, 1, 9);

        grid.add(new Label("CEP:"), 2, 0); grid.add(txtCep, 3, 0);
        grid.add(new Label("Cidade:"), 2, 1); grid.add(txtCidade, 3, 1);
        grid.add(new Label("Rua/Av:"), 2, 2); grid.add(txtRua, 3, 2);
        grid.add(new Label("Bairro:"), 2, 3); grid.add(txtBairro, 3, 3);
        grid.add(new Label("Número:"), 2, 4); grid.add(txtNumero, 3, 4);

        grid.add(new Separator(), 2, 5, 2, 1);
        grid.add(new Label("--- DOC OBRIGATÓRIO ---"), 2, 6, 2, 1);
        grid.add(new Label("Tipo de Doc:"), 2, 7); grid.add(comboTipoDoc, 3, 7);
        grid.add(new Label("Data Emissão Doc:"), 2, 8); grid.add(txtDataEmissaoDoc, 3, 8);
        grid.add(new Label("URL do Arquivo:"), 2, 9); grid.add(txtUrlArquivo, 3, 9);

        Button btnSalvar = new Button("Salvar OSC");
        Button btnVoltar = new Button("Voltar");

        btnSalvar.setOnAction(e -> executarCadastro());
        btnVoltar.setOnAction(e -> voltarParaLogin());

        HBox botoes = new HBox(10, btnSalvar, btnVoltar);
        grid.add(botoes, 1, 11, 2, 1);

        this.scene = new Scene(grid, 700, 500);
    }

    public Scene getScene() { return this.scene; }

    private void executarCadastro() {
        try {
            // As validações de letras em campos numéricos ocorrem dentro dos ValueObjects (Cnpj, Cep, etc)
            Cnpj cnpj = new Cnpj(txtCnpj.getText());
            Email email = new Email(txtEmail.getText());
            Telefone telefone = new Telefone(txtTelefone.getText());
            Cep cep = new Cep(txtCep.getText());
            Endereco endereco = new Endereco(cep, txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText());

            // Exigência: Formatação e captura de Datas em DD/MM/AAAA
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataFundacao;
            LocalDate dataEmissaoDoc;
            try {
                dataFundacao = LocalDate.parse(txtDataFundacao.getText(), formatador);
                dataEmissaoDoc = LocalDate.parse(txtDataEmissaoDoc.getText(), formatador);
            } catch (DateTimeParseException ex) {
                throw new DomainException("As datas devem estar obrigatoriamente no formato DD/MM/AAAA.");
            }

            String catSelecionada = comboCategoria.getValue();
            if (catSelecionada == null) throw new DomainException("Selecione uma Categoria.");
            Categoria categoria = Categoria.valueOf(catSelecionada);

            String tipoDocSelecionado = comboTipoDoc.getValue();
            if (tipoDocSelecionado == null) throw new DomainException("Selecione um Tipo de Documento.");
            TipoDoc tipoDoc = TipoDoc.valueOf(tipoDocSelecionado);

            // Cria o documento que será validado pela regra de negócio
            DocOsc docInicial = new DocOsc(dataEmissaoDoc, tipoDoc, txtUrlArquivo.getText());

            // Aciona a Service
            service.cadastrarOsc(
                    endereco, email, telefone, cnpj, txtNomeLegal.getText(), txtNomeComercial.getText(),
                    dataFundacao, categoria, txtChavePix.getText(), docInicial, txtLogin.getText(), txtSenha.getText()
            );

            new Alert(Alert.AlertType.INFORMATION, "OSC cadastrada com sucesso! O documento foi enviado para validação.").showAndWait();
            voltarParaLogin();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Ocorreu um erro inesperado: " + ex.getMessage()).showAndWait();
        }
    }

    private void voltarParaLogin() {
        UsuarioRepository repo = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(repo);
        LoginView loginView = new LoginView(stage, usuarioService);
        stage.setScene(loginView.getScene());
    }
}