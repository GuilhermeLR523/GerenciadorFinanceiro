package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Transacao;
import br.com.cotemig.gerenciadorfinanceiro.service.TransacaoService;
import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TransacaoController {

    @FXML
    private TableView<Transacao> tabelaTransacoes;

    @FXML
    private TableColumn<Transacao, String> colunaData;

    @FXML
    private TableColumn<Transacao, String> colunaDescricao;

    @FXML
    private TableColumn<Transacao, String> colunaTipo;

    @FXML
    private TableColumn<Transacao, String> colunaCategoria;

    @FXML
    private TableColumn<Transacao, String> colunaConta;

    @FXML
    private TableColumn<Transacao, String> colunaValor;

    @FXML
    private Label labelMensagem;

    private final TransacaoService transacaoService =
            new TransacaoService();

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTransacoes();
    }

    @FXML
    private void abrirNovaTransacao() {
        try {
            ViewUtil.abrirModal(
                    "nova-transacao-view.fxml",
                    "Nova Transação",
                    tabelaTransacoes.getScene().getWindow()
            );

            carregarTransacoes();

        } catch (IOException | IllegalStateException e) {
            labelMensagem.setText("Não foi possível abrir o formulário de nova transação.");
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirTransacao() {
        Transacao transacaoSelecionada =
                tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (transacaoSelecionada == null) {
            labelMensagem.setText("Selecione uma transação na tabela.");
            return;
        }

        boolean confirmou = ViewUtil.confirmarExclusao(
                "Excluir Transação",
                "Tem certeza de que deseja excluir a transação \"" +
                        transacaoSelecionada.getDescricao() +
                        "\"?\n\nEsta ação não poderá ser desfeita."
        );

        if (!confirmou) {
            return;
        }

        try {
            transacaoService.excluir(transacaoSelecionada.getId());
            labelMensagem.setText("Transação excluída com sucesso.");
            carregarTransacoes();

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    private void configurarColunas() {
        colunaData.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getData().format(formatoData)
                )
        );

        colunaDescricao.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getDescricao()
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

        colunaCategoria.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getCategoria().getNome()
                )
        );

        colunaConta.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getConta().getNome()
                )
        );

        colunaValor.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatoMoeda.format(dados.getValue().getValor())
                )
        );
    }

    private void carregarTransacoes() {
        tabelaTransacoes.setItems(
                FXCollections.observableArrayList(
                        transacaoService.listarTodas()
                )
        );
    }
}
