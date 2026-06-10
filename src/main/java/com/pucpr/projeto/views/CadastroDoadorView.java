package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.PessoaFisicaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CadastroDoadorView {
    private final Stage stage;
    private final PessoaFisicaService service;
    private Scene scene;

    private TextField txtNome, txtCpf, txtEmail, txtTelefone, txtDataNasc, txtCep, txtRua, txtNumero, txtLogin,
            txtBairro, txtCidade;
    private PasswordField txtSenha;
    private ComboBox<String> comboGenero;

    public CadastroDoadorView(Stage stage, PessoaFisicaService service) {
        this.stage = stage;
        this.service = service;
        inicializarTela();
    }

    private void inicializarTela() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        txtNome = new TextField();
        txtCpf = new TextField();
        txtEmail = new TextField();
        txtTelefone = new TextField();
        txtDataNasc = new TextField();

        txtCep = new TextField();
        txtCidade = new TextField();
        txtRua = new TextField();
        txtBairro = new TextField();
        txtNumero = new TextField();

        txtLogin = new TextField();
        txtSenha = new PasswordField();

        comboGenero = new ComboBox<>();
        comboGenero.getItems().addAll("Masculino", "Feminino", "Outro");

        grid.add(new Label("Nome Completo:"), 0, 0); grid.add(txtNome, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);           grid.add(txtCpf, 1, 1);
        grid.add(new Label("E-mail:"), 0, 2);        grid.add(txtEmail, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3);      grid.add(txtTelefone, 1, 3);
        grid.add(new Label("Data Nasc:"), 0, 4);     grid.add(txtDataNasc, 1, 4);
        grid.add(new Label("Gênero:"), 0, 5);        grid.add(comboGenero, 1, 5);

        grid.add(new Label("CEP:"), 0, 6);           grid.add(txtCep, 1, 6);
        grid.add(new Label("Cidade:"), 0, 7);        grid.add(txtCidade, 1, 7);
        grid.add(new Label("Rua/Av:"), 0, 8);        grid.add(txtRua, 1, 8);
        grid.add(new Label("Bairro:"), 0, 9);        grid.add(txtBairro, 1, 9);
        grid.add(new Label("Número:"), 0, 10);       grid.add(txtNumero, 1, 10);

        grid.add(new Label("Login:"), 0, 11);        grid.add(txtLogin, 1, 11);
        grid.add(new Label("Senha:"), 0, 12);        grid.add(txtSenha, 1, 12);

        Button btnSalvar = new Button("Salvar Cadastro");
        Button btnVoltar = new Button("Voltar para o Login");

        btnSalvar.setOnAction(e -> executarCadastro());
        btnVoltar.setOnAction(e -> voltarParaLogin());

        HBox botoes = new HBox(10, btnSalvar, btnVoltar);
        grid.add(botoes, 1, 13);

        this.scene = new Scene(grid, 500, 550);
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
            Endereco endereco = new Endereco(cep, txtRua.getText(), txtBairro.getText(), txtCidade.getText(),
                    txtNumero.getText());

            String generoSelecionado = comboGenero.getValue() != null ? comboGenero.getValue() : "Não Informado";
            Genero genero = Genero.valueOf(generoSelecionado.toUpperCase());

            service.cadastrar(
                    txtNome.getText(), cpf, email, telefone, dta, genero,
                    endereco, txtLogin.getText(), txtSenha.getText()
            );

            new Alert(Alert.AlertType.INFORMATION, "Cadastro realizado com sucesso!").showAndWait();
            voltarParaLogin();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (IllegalArgumentException ex) {
            new Alert(Alert.AlertType.WARNING, "Por favor, verifique as opções selecionadas.").showAndWait();
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
