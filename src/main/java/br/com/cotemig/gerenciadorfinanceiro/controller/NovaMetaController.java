package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.service.MetaService;
import br.com.cotemig.gerenciadorfinanceiro.util.ValorUtil;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class NovaMetaController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoValorObjetivo;

    @FXML
    private DatePicker campoDataFim;

    @FXML
    private Label labelMensagem;

    private final MetaService metaService = new MetaService();

    @FXML
    private void cadastrar() {
        try {
            BigDecimal valorObjetivo =
                    ValorUtil.converterParaBigDecimal(
                            campoValorObjetivo.getText()
                    );

            metaService.cadastrar(
                    campoNome.getText(),
                    valorObjetivo,
                    campoDataFim.getValue()
            );

            fecharJanela();

        } catch (NumberFormatException e) {
            labelMensagem.setText("Informe um valor objetivo válido.");

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}
