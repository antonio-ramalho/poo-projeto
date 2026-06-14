package com.pucpr.projeto.views;

import com.pucpr.projeto.domain.entities.Doador;
import com.pucpr.projeto.domain.entities.Osc;
import com.pucpr.projeto.domain.entities.Usuario;
import com.pucpr.projeto.domain.valueObjects.*;
import com.pucpr.projeto.enums.Categoria;
import com.pucpr.projeto.enums.Genero;
import com.pucpr.projeto.exceptions.DomainException;
import com.pucpr.projeto.repositories.DoadorRepository;
import com.pucpr.projeto.repositories.OscRepository;
import com.pucpr.projeto.repositories.UsuarioRepository;
import com.pucpr.projeto.services.DoadorService;
import com.pucpr.projeto.services.PessoaJuridicaService;
import com.pucpr.projeto.services.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;

public class HomeDoadorView {
    private final Stage stage;
    private final Usuario usuarioLogado;
    private Doador doadorLogado;
    private Scene scene;
    private DoadorService pfService; // DoadorService gerenciando o doador
    private UsuarioService usuarioService;
    private PessoaJuridicaService pjService;
    private TextField txtNome, txtEmail, txtTelefone;
    private TextField txtCep, txtCidade, txtRua, txtBairro, txtNumero;
    private ComboBox<String> comboGenero, comboPreferencia;
    private CheckBox chkAnonimato;

    public HomeDoadorView(Stage stage, Usuario usuarioLogado) {
        this.stage = stage;
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService(new UsuarioRepository());
        this.pfService = new DoadorService(new UsuarioRepository(), new DoadorRepository());
        this.pjService = new PessoaJuridicaService(new UsuarioRepository(), new OscRepository());
        this.doadorLogado = pfService.buscarPorId(usuarioLogado.getId());

        inicializarTela();
    }

    private void inicializarTela() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        HBox cabecalho = new HBox(20);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        Label lblBoasVindas = new Label("Bem-vindo(a), " + doadorLogado.getNome() + "!");
        lblBoasVindas.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setOnAction(e -> fazerLogout());
        cabecalho.getChildren().addAll(lblBoasVindas, btnSair);

        TabPane painelAbas = new TabPane();
        painelAbas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        painelAbas.getTabs().add(criarAbaExplorar());
        painelAbas.getTabs().add(criarAbaPerfil());

        layout.getChildren().addAll(cabecalho, new Separator(), painelAbas);
        this.scene = new Scene(layout, 750, 600); // Ajuste na largura
    }

    private Tab criarAbaExplorar() {
        Tab aba = new Tab("Explorar OSCs");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label lblInstrucao = new Label("Conheça as Organizações cadastradas na plataforma:");

        TableView<Osc> tabela = new TableView<>();

        TableColumn<Osc, String> colNomeComercial = new TableColumn<>("Nome da Instituição");
        colNomeComercial.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomeComercial()));
        colNomeComercial.setPrefWidth(200);

        TableColumn<Osc, String> colCategoria = new TableColumn<>("Área de Atuação");
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtuacao().name()));
        colCategoria.setPrefWidth(150);

        tabela.getColumns().addAll(colNomeComercial, colCategoria);
        tabela.getItems().setAll(pjService.buscarTodas());

        layout.getChildren().addAll(lblInstrucao, tabela);
        aba.setContent(layout);
        return aba;
    }

    private Tab criarAbaPerfil() {
        Tab aba = new Tab("Meu Perfil");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(12);
        grid.setHgap(15);

        txtNome = new TextField(doadorLogado.getNome());
        txtEmail = new TextField(doadorLogado.getEmail().getEnderecoEmail());
        txtTelefone = new TextField(doadorLogado.getTelefone().getNumeroTelefone());

        comboGenero = new ComboBox<>();
        comboGenero.getItems().addAll("MASCULINO", "FEMININO", "OUTRO", "NAO_INFORMADO");
        comboGenero.setValue(doadorLogado.getGenero() != null ? doadorLogado.getGenero().toUpperCase() : "NAO_INFORMADO");

        comboPreferencia = new ComboBox<>();
        Arrays.stream(Categoria.values()).forEach(c -> comboPreferencia.getItems().add(c.name()));
        comboPreferencia.setValue(doadorLogado.getCategoria() != null ? doadorLogado.getCategoria().name() : null);

        chkAnonimato = new CheckBox("Manter minhas doações anônimas");
        chkAnonimato.setSelected(doadorLogado.getAnonimato());

        txtCep = new TextField(doadorLogado.getEndereco().getNumeroCep().getNumeroCep());
        txtRua = new TextField(doadorLogado.getEndereco().getRua());
        txtBairro = new TextField(doadorLogado.getEndereco().getBairro());
        txtCidade = new TextField(doadorLogado.getEndereco().getCidade());
        txtNumero = new TextField(doadorLogado.getEndereco().getNumeroEndereco());

        grid.add(new Label("Nome Completo:"), 0, 0); grid.add(txtNome, 1, 0);
        grid.add(new Label("E-mail:"), 0, 1);        grid.add(txtEmail, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);      grid.add(txtTelefone, 1, 2);
        grid.add(new Label("Gênero:"), 0, 3);        grid.add(comboGenero, 1, 3);
        grid.add(new Label("Causa Preferida:"), 0, 4); grid.add(comboPreferencia, 1, 4);
        grid.add(chkAnonimato, 1, 5);

        grid.add(new Label("CEP:"), 2, 0);           grid.add(txtCep, 3, 0);
        grid.add(new Label("Cidade:"), 2, 1);        grid.add(txtCidade, 3, 1);
        grid.add(new Label("Rua/Av:"), 2, 2);        grid.add(txtRua, 3, 2);
        grid.add(new Label("Bairro:"), 2, 3);        grid.add(txtBairro, 3, 3);
        grid.add(new Label("Número:"), 2, 4);        grid.add(txtNumero, 3, 4);

        Button btnAtualizar = new Button("Salvar Alterações");
        btnAtualizar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAtualizar.setOnAction(e -> atualizarDados());

        Button btnExcluir = new Button("Excluir Minha Conta");
        btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnExcluir.setOnAction(e -> excluirConta());

        HBox botoes = new HBox(15, btnAtualizar, btnExcluir);
        grid.add(botoes, 0, 7, 4, 1); // Expandido para cruzar as colunas

        aba.setContent(grid);
        return aba;
    }

    private void atualizarDados() {
        try {
            Telefone telefone = txtTelefone.getText().isEmpty() ? null : new Telefone(txtTelefone.getText());
            Email email = txtEmail.getText().isEmpty() ? null : new Email(txtEmail.getText());
            Genero genero = Genero.valueOf(comboGenero.getValue());

            Categoria preferencia = comboPreferencia.getValue() != null ? Categoria.valueOf(comboPreferencia.getValue()) : null;
            boolean anonimato = chkAnonimato.isSelected();

            Endereco endereco = null;
            if (!txtCep.getText().isEmpty() && !txtRua.getText().isEmpty()) {
                Cep cep = new Cep(txtCep.getText());
                endereco = new Endereco(cep, txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText());
            }

            pfService.atualizar(telefone, email, txtNome.getText(), genero, doadorLogado.getId(), endereco, preferencia, anonimato);

            new Alert(Alert.AlertType.INFORMATION, "Perfil atualizado com sucesso!").showAndWait();

            this.doadorLogado = pfService.buscarPorId(usuarioLogado.getId());

        } catch (DomainException ex) {
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Verifique os dados informados: " + ex.getMessage()).showAndWait();
        }
    }

    private void excluirConta() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Excluir Conta");
        confirmacao.setHeaderText("Você tem certeza que deseja excluir sua conta?");
        confirmacao.setContentText("Esta ação é irreversível. Seu acesso e todos os seus dados serão apagados.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                pfService.excluir(doadorLogado.getId());
                usuarioService.excluir(usuarioLogado.getId());

                new Alert(Alert.AlertType.INFORMATION, "Conta excluída com sucesso.").showAndWait();
                fazerLogout();

            } catch (DomainException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    private void fazerLogout() {
        LoginView loginView = new LoginView(stage, usuarioService);
        stage.setScene(loginView.getScene());
    }
}