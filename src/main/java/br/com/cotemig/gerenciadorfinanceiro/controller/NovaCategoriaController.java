package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.service.CategoriaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class NovaCategoriaController {

    @FXML
    private TextField campoNome;

    @FXML
    private ComboBox<TipoTransacao> comboTipo;

    @FXML
    private Label labelMensagem;

    private final CategoriaService categoriaService =
            new CategoriaService();

    @FXML
    public void initialize() {
        configurarTipos();
    }

    @FXML
    private void cadastrar() {
        try {
            categoriaService.cadastrar(
                    campoNome.getText(),
                    comboTipo.getValue(),
                    null,
                    null
            );

            fecharJanela();

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void configurarTipos() {
        comboTipo.setItems(
                FXCollections.observableArrayList(TipoTransacao.values())
        );

        comboTipo.setConverter(
                new StringConverter<>() {
                    @Override
                    public String toString(TipoTransacao tipo) {
                        if (tipo == null) {
                            return "";
                        }

                        return switch (tipo) {
                            case RECEITA -> "Receita";
                            case DESPESA -> "Despesa";
                        };
                    }

                    @Override
                    public TipoTransacao fromString(String texto) {
                        return null;
                    }
                }
        );
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}
