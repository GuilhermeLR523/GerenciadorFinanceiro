package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoConta;
import br.com.cotemig.gerenciadorfinanceiro.service.ContaService;
import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class ContaController {

    @FXML
    private TableView<Conta> tabelaContas;

    @FXML
    private TableColumn<Conta, String> colunaNome;

    @FXML
    private TableColumn<Conta, String> colunaTipo;

    @FXML
    private TableColumn<Conta, String> colunaSaldo;

    @FXML
    private TableColumn<Conta, String> colunaStatus;

    @FXML
    private Label labelMensagem;

    private final ContaService contaService = new ContaService();

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    @FXML
    public void initialize() {
        configurarColunas();
        carregarContas();
    }

    @FXML
    private void abrirNovaConta() {
        try {
            ViewUtil.abrirModal(
                    "nova-conta-view.fxml",
                    "Nova Conta",
                    tabelaContas.getScene().getWindow()
            );

            carregarContas();

        } catch (IOException | IllegalStateException e) {
            labelMensagem.setText("Não foi possível abrir o formulário de nova conta.");
            e.printStackTrace();
        }
    }

    @FXML
    private void alterarStatus() {
        Conta contaSelecionada =
                tabelaContas.getSelectionModel().getSelectedItem();

        if (contaSelecionada == null) {
            labelMensagem.setText("Selecione uma conta na tabela.");
            return;
        }

        try {
            if (contaSelecionada.getAtiva()) {
                contaService.desativar(contaSelecionada.getId());
                labelMensagem.setText("Conta desativada com sucesso.");
            } else {
                contaService.ativar(contaSelecionada.getId());
                labelMensagem.setText("Conta reativada com sucesso.");
            }

            carregarContas();

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void excluirConta() {
        Conta contaSelecionada =
                tabelaContas.getSelectionModel().getSelectedItem();

        if (contaSelecionada == null) {
            labelMensagem.setText("Selecione uma conta para excluir.");
            return;
        }

        boolean confirmou = ViewUtil.confirmarExclusao(
                "Excluir Conta",
                "Tem certeza de que deseja excluir a conta \"" +
                        contaSelecionada.getNome() +
                        "\"?\n\nEsta ação não poderá ser desfeita."
        );

        if (!confirmou) {
            return;
        }

        try {
            contaService.excluir(contaSelecionada.getId());
            labelMensagem.setText("Conta excluída com sucesso.");
            carregarContas();

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
                        formatarTipo(dados.getValue().getTipo())
                )
        );

        colunaSaldo.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatoMoeda.format(dados.getValue().getSaldoAtual())
                )
        );

        colunaStatus.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getAtiva() ? "Ativa" : "Inativa"
                )
        );
    }

    private void carregarContas() {
        tabelaContas.setItems(
                FXCollections.observableArrayList(
                        contaService.listarTodas()
                )
        );
    }

    private String formatarTipo(TipoConta tipo) {
        return switch (tipo) {
            case CONTA_CORRENTE -> "Conta Corrente";
            case POUPANCA -> "Poupança";
            case DINHEIRO -> "Dinheiro";
            case CARTEIRA_DIGITAL -> "Carteira Digital";
        };
    }
}
