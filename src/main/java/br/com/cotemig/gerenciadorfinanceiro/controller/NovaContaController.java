package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.TipoConta;
import br.com.cotemig.gerenciadorfinanceiro.service.ContaService;
import br.com.cotemig.gerenciadorfinanceiro.util.ValorUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;

public class NovaContaController {

    @FXML
    private TextField campoNome;

    @FXML
    private ComboBox<TipoConta> comboTipo;

    @FXML
    private TextField campoSaldoInicial;

    @FXML
    private Label labelMensagem;

    private final ContaService contaService = new ContaService();

    @FXML
    public void initialize() {
        configurarTipos();
    }

    @FXML
    private void cadastrar() {
        try {
            BigDecimal saldoInicial =
                    ValorUtil.converterParaBigDecimal(
                            campoSaldoInicial.getText()
                    );

            contaService.cadastrar(
                    campoNome.getText(),
                    comboTipo.getValue(),
                    saldoInicial
            );

            fecharJanela();

        } catch (NumberFormatException e) {
            labelMensagem.setText("Informe um saldo inicial válido.");

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
                FXCollections.observableArrayList(TipoConta.values())
        );

        comboTipo.setConverter(
                new StringConverter<>() {
                    @Override
                    public String toString(TipoConta tipo) {
                        if (tipo == null) {
                            return "";
                        }

                        return switch (tipo) {
                            case CONTA_CORRENTE -> "Conta Corrente";
                            case POUPANCA -> "Poupança";
                            case DINHEIRO -> "Dinheiro";
                            case CARTEIRA_DIGITAL -> "Carteira Digital";
                        };
                    }

                    @Override
                    public TipoConta fromString(String texto) {
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
