package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane areaConteudo;

    @FXML
    public void initialize() {
        abrirDashboard();
    }

    @FXML
    private void abrirDashboard() {
        carregarTela("dashboard-view.fxml");
    }

    @FXML
    private void abrirContas() {
        carregarTela("contas-view.fxml");
    }

    @FXML
    private void abrirTransacoes() {
        carregarTela("transacoes-view.fxml");
    }

    @FXML
    private void abrirCategorias() {
        carregarTela("categorias-view.fxml");
    }

    @FXML
    private void abrirMetas() {
        carregarTela("metas-view.fxml");
    }

    private void carregarTela(String arquivoFxml) {
        try {
            Node tela = ViewUtil.carregarFXML(arquivoFxml);
            areaConteudo.getChildren().setAll(tela);

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
