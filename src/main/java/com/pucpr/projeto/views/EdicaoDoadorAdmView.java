package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.PessoaFisica;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.Cep;
import com.pucpr.projeto.domain.valueObjects.Email;
import com.pucpr.projeto.domain.valueObjects.Endereco;
import com.pucpr.projeto.domain.valueObjects.Telefone;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.services.PessoaFisicaService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EdicaoDoadorAdmView {
    private final Stage stage;
    private final PessoaFisica doador;
    private final PessoaFisicaService service;
    private final Usuario adminLogado; // Para podermos voltar para a HomeAdm
    private Scene scene;

    private TextField txtNome, txtEmail, txtTelefone, txtCep, txtRua, txtBairro, txtCidade, txtNumero;
    private ComboBox<String> comboGenero;

    public EdicaoDoadorAdmView(Stage stage, PessoaFisica doador, PessoaFisicaService service, Usuario adminLogado) {
        this.stage = stage;
        this.doador = doador;
        this.service = service;
        this.adminLogado = adminLogado;
        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titulo = new Label("Editando Doador: " + doador.getNome());
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        txtNome = new TextField(doador.getNome());
        txtEmail = new TextField(doador.getEmail().getEnderecoEmail());
        txtTelefone = new TextField(doador.getTelefone().getNumeroTelefone());

        comboGenero = new ComboBox<>();
        comboGenero.getItems().addAll("MASCULINO", "FEMININO", "OUTRO", "NAO_INFORMADO");
        comboGenero.setValue(doador.getGenero() != null ? doador.getGenero().toUpperCase() : "NAO_INFORMADO");

        txtCep = new TextField(doador.getEndereco().getNumeroCep().getNumeroCep());
        txtRua = new TextField(doador.getEndereco().getRua() != null ? doador.getEndereco().getRua() : "");
        txtBairro = new TextField(doador.getEndereco().getBairro() != null ? doador.getEndereco().getBairro() : "");
        txtCidade = new TextField(doador.getEndereco().getCidade() != null ? doador.getEndereco().getCidade() : "");
        txtNumero = new TextField(doador.getEndereco().getNumeroEndereco() != null ? doador.getEndereco().getNumeroEndereco() : "");

        grid.add(new Label("Nome Completo:"), 0, 0); grid.add(txtNome, 1, 0);
        grid.add(new Label("E-mail:"), 0, 1);        grid.add(txtEmail, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);      grid.add(txtTelefone, 1, 2);
        grid.add(new Label("Gênero:"), 0, 3);        grid.add(comboGenero, 1, 3);
        grid.add(new Label("CEP:"), 0, 4);           grid.add(txtCep, 1, 4);
        grid.add(new Label("Rua/Av:"), 0, 5);        grid.add(txtRua, 1, 5);
        grid.add(new Label("Número:"), 0, 6);        grid.add(txtNumero, 1, 6);
        grid.add(new Label("Bairro:"), 0, 7);        grid.add(txtBairro, 1, 7);
        grid.add(new Label("Cidade:"), 0, 8);        grid.add(txtCidade, 1, 8);

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarEdicao());

        Button btnCancelar = new Button("Cancelar e Voltar");
        btnCancelar.setOnAction(e -> voltarParaHome());

        HBox botoes = new HBox(15, btnSalvar, btnCancelar);

        layout.getChildren().addAll(titulo, new Separator(), grid, botoes);
        this.scene = new Scene(layout, 500, 500);
    }

    private void salvarEdicao() {
        try {
            Telefone telefone = txtTelefone.getText().isEmpty() ? null : new Telefone(txtTelefone.getText());
            Email email = txtEmail.getText().isEmpty() ? null : new Email(txtEmail.getText());
            Genero genero = Genero.valueOf(comboGenero.getValue());

            Endereco endereco = null;
            if (!txtCep.getText().isEmpty()) {
                Cep cep = new Cep(txtCep.getText());
                endereco = new Endereco(cep, txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText());
            }

            service.atualizar(telefone, email, txtNome.getText(), genero, doador.getId(), endereco);

            new Alert(Alert.AlertType.INFORMATION, "Doador atualizado com sucesso!").showAndWait();
            voltarParaHome();

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Erro ao atualizar: " + ex.getMessage()).showAndWait();
        }
    }

    private void voltarParaHome() {
        HomeAdmView home = new HomeAdmView(stage, adminLogado);
        stage.setScene(home.getScene());
    }

    public Scene getScene() { return this.scene; }
}
