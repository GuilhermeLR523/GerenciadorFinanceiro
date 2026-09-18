package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Categoria;
import br.com.cotemig.gerenciadorfinanceiro.service.CategoriaService;
import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class CategoriaController {

    @FXML
    private TableView<Categoria> tabelaCategorias;

    @FXML
    private TableColumn<Categoria, String> colunaNome;

    @FXML
    private TableColumn<Categoria, String> colunaTipo;

    @FXML
    private TableColumn<Categoria, String> colunaStatus;

    @FXML
    private Label labelMensagem;

    private final CategoriaService categoriaService =
            new CategoriaService();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarCategorias();
    }

    @FXML
    private void abrirNovaCategoria() {
        try {
            ViewUtil.abrirModal(
                    "nova-categoria-view.fxml",
                    "Nova Categoria",
                    tabelaCategorias.getScene().getWindow()
            );

            carregarCategorias();

        } catch (IOException | IllegalStateException e) {
            labelMensagem.setText("Não foi possível abrir o formulário de nova categoria.");
            e.printStackTrace();
        }
    }

    @FXML
    private void alterarStatus() {
        Categoria categoriaSelecionada =
                tabelaCategorias.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            labelMensagem.setText("Selecione uma categoria na tabela.");
            return;
        }

        try {
            if (categoriaSelecionada.getAtiva()) {
                categoriaService.desativar(categoriaSelecionada.getId());
                labelMensagem.setText("Categoria desativada com sucesso.");
            } else {
                categoriaService.ativar(categoriaSelecionada.getId());
                labelMensagem.setText("Categoria reativada com sucesso.");
            }

            carregarCategorias();

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void excluirCategoria() {
        Categoria categoriaSelecionada =
                tabelaCategorias.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            labelMensagem.setText("Selecione uma categoria para excluir.");
            return;
        }

        boolean confirmou = ViewUtil.confirmarExclusao(
                "Excluir Categoria",
                "Tem certeza de que deseja excluir a categoria \"" +
                        categoriaSelecionada.getNome() +
                        "\"?\n\nEsta ação não poderá ser desfeita."
        );

        if (!confirmou) {
            return;
        }

        try {
            categoriaService.excluir(categoriaSelecionada.getId());
            labelMensagem.setText("Categoria excluída com sucesso.");
            carregarCategorias();

        } catch (IllegalStateException | IllegalArgumentException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    private void configurarColunas() {
        colunaNome.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getNome()
                )
        );

        colunaTipo.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        switch (dados.getValue().getTipo()) {
                            case RECEITA -> "Receita";
                            case DESPESA -> "Despesa";
                        }
                )
        );

        colunaStatus.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getAtiva() ? "Ativa" : "Inativa"
                )
        );
    }

    private void carregarCategorias() {
        tabelaCategorias.setItems(
                FXCollections.observableArrayList(
                        categoriaService.listarTodas()
                )
        );
    }
}
