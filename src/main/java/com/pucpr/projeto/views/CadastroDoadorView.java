package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.DoadorService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class CadastroDoadorView {
    private final Stage stage;
    private final DoadorService service;
    private Scene scene;

    private TextField txtNome, txtCpf, txtEmail, txtTelefone, txtDataNasc;
    private TextField txtCep, txtCidade, txtRua, txtBairro, txtNumero, txtLogin;
    private PasswordField txtSenha;

    private ComboBox<String> comboGenero;

    private ComboBox<String> comboPreferencia;
    private CheckBox chkAnonimato;

    public CadastroDoadorView(Stage stage, DoadorService service) {
        this.stage = stage;
        this.service = service;
        inicializarTela();
    }

    private void inicializarTela() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(12);
        grid.setHgap(15);

        txtNome = new TextField();
        txtCpf = new TextField();
        txtEmail = new TextField();
        txtTelefone = new TextField();
        txtDataNasc = new TextField();
        txtDataNasc.setPromptText("DD/MM/AAAA");

        txtCep = new TextField();
        txtCidade = new TextField();
        txtRua = new TextField();
        txtBairro = new TextField();
        txtNumero = new TextField();

        txtLogin = new TextField();
        txtSenha = new PasswordField();

        comboGenero = new ComboBox<>();
        comboGenero.getItems().addAll("Masculino", "Feminino", "Outro");

        comboPreferencia = new ComboBox<>();
        Arrays.stream(Categoria.values()).forEach(c -> comboPreferencia.getItems().add(c.name()));

        chkAnonimato = new CheckBox("Manter minhas doações anônimas");

        grid.add(new Label("Nome Completo:"), 0, 0); grid.add(txtNome, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);           grid.add(txtCpf, 1, 1);
        grid.add(new Label("E-mail:"), 0, 2);        grid.add(txtEmail, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3);      grid.add(txtTelefone, 1, 3);
        grid.add(new Label("Data Nasc:"), 0, 4);     grid.add(txtDataNasc, 1, 4);
        grid.add(new Label("Gênero:"), 0, 5);        grid.add(comboGenero, 1, 5);

        grid.add(new Separator(), 0, 6, 2, 1);
        grid.add(new Label("Causa de Preferência:"), 0, 7); grid.add(comboPreferencia, 1, 7);
        grid.add(chkAnonimato, 1, 8);

        grid.add(new Label("CEP:"), 2, 0);           grid.add(txtCep, 3, 0);
        grid.add(new Label("Cidade:"), 2, 1);        grid.add(txtCidade, 3, 1);
        grid.add(new Label("Rua/Av:"), 2, 2);        grid.add(txtRua, 3, 2);
        grid.add(new Label("Bairro:"), 2, 3);        grid.add(txtBairro, 3, 3);
        grid.add(new Label("Número:"), 2, 4);        grid.add(txtNumero, 3, 4);

        grid.add(new Separator(), 2, 5, 2, 1);
        grid.add(new Label("Login:"), 2, 6);         grid.add(txtLogin, 3, 6);
        grid.add(new Label("Senha:"), 2, 7);         grid.add(txtSenha, 3, 7);

        Button btnSalvar = new Button("Salvar Cadastro");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnVoltar = new Button("Voltar para o Login");

        btnSalvar.setOnAction(e -> executarCadastro());
        btnVoltar.setOnAction(e -> voltarParaLogin());

        HBox botoes = new HBox(15, btnSalvar, btnVoltar);
        grid.add(botoes, 1, 10, 3, 1);

        this.scene = new Scene(grid, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }

    private void executarCadastro() {
        try {
            Cpf cpf = new Cpf(txtCpf.getText());
            Email email = new Email(txtEmail.getText());
            Telefone telefone = new Telefone(txtTelefone.getText());
            DataNascimento dta = new DataNascimento(txtDataNasc.getText());
            Cep cep = new Cep(txtCep.getText());
            Endereco endereco = new Endereco(cep, txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText());

            String generoSelecionado = comboGenero.getValue() != null ? comboGenero.getValue() : "Não Informado";
            Genero genero = Genero.valueOf(generoSelecionado.toUpperCase());

            String catSelecionada = comboPreferencia.getValue();
            if (catSelecionada == null) throw new DomainException("Por favor, selecione uma causa de preferência.");
            Categoria preferencia = Categoria.valueOf(catSelecionada);

            boolean anonimato = chkAnonimato.isSelected();

            service.cadastrar(
                    txtNome.getText(), cpf, email, telefone, dta, genero,
                    endereco, txtLogin.getText(), txtSenha.getText(),
                    preferencia, anonimato
            );

            new Alert(Alert.AlertType.INFORMATION, "Cadastro realizado com sucesso!").showAndWait();
            voltarParaLogin();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (IllegalArgumentException ex) {
            new Alert(Alert.AlertType.WARNING, "Por favor, verifique se todas as opções (Gênero/Categoria) foram selecionadas.").showAndWait();
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
