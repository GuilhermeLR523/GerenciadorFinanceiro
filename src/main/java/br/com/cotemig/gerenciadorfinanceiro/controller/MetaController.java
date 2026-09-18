package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Meta;
import br.com.cotemig.gerenciadorfinanceiro.service.MetaService;
import br.com.cotemig.gerenciadorfinanceiro.util.ValorUtil;
import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class MetaController {

    @FXML
    private TableView<Meta> tabelaMetas;

    @FXML
    private TableColumn<Meta, String> colunaNome;

    @FXML
    private TableColumn<Meta, String> colunaObjetivo;

    @FXML
    private TableColumn<Meta, String> colunaAtual;

    @FXML
    private TableColumn<Meta, String> colunaProgresso;

    @FXML
    private TableColumn<Meta, String> colunaPrazo;

    @FXML
    private TableColumn<Meta, String> colunaStatus;

    @FXML
    private Label labelMensagem;

    private final MetaService metaService = new MetaService();

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarColunas();
        carregarMetas();
    }

    @FXML
    private void abrirNovaMeta() {
        try {
            ViewUtil.abrirModal(
                    "nova-meta-view.fxml",
                    "Nova Meta",
                    tabelaMetas.getScene().getWindow()
            );

            carregarMetas();

        } catch (IOException | IllegalStateException e) {
            labelMensagem.setText("Não foi possível abrir o formulário de nova meta.");
            e.printStackTrace();
        }
    }

    @FXML
    private void adicionarValor() {
        Meta metaSelecionada =
                tabelaMetas.getSelectionModel().getSelectedItem();

        if (metaSelecionada == null) {
            labelMensagem.setText("Selecione uma meta na tabela.");
            return;
        }

        if (!metaSelecionada.getAtiva()) {
            labelMensagem.setText(
                    "Não é possível adicionar valor a uma meta inativa."
            );
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Valor");
        dialog.setHeaderText(
                "Adicionar valor à meta: " + metaSelecionada.getNome()
        );
        dialog.setContentText("Valor:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isEmpty()) {
            return;
        }

        try {
            BigDecimal valor = ValorUtil.converterParaBigDecimal(resultado.get());

            metaService.adicionarValor(
                    metaSelecionada.getId(),
                    valor
            );

            labelMensagem.setText("Valor adicionado com sucesso.");
            carregarMetas();

        } catch (NumberFormatException e) {
            labelMensagem.setText("Informe um valor válido.");

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void alterarStatus() {
        Meta metaSelecionada =
                tabelaMetas.getSelectionModel().getSelectedItem();

        if (metaSelecionada == null) {
            labelMensagem.setText("Selecione uma meta na tabela.");
            return;
        }

        try {
            if (metaSelecionada.getAtiva()) {
                metaService.desativar(metaSelecionada.getId());
                labelMensagem.setText("Meta desativada com sucesso.");
            } else {
                metaService.ativar(metaSelecionada.getId());
                labelMensagem.setText("Meta reativada com sucesso.");
            }

            carregarMetas();

        } catch (RuntimeException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    @FXML
    private void excluirMeta() {
        Meta metaSelecionada =
                tabelaMetas.getSelectionModel().getSelectedItem();

        if (metaSelecionada == null) {
            labelMensagem.setText("Selecione uma meta para excluir.");
            return;
        }

        boolean confirmou = ViewUtil.confirmarExclusao(
                "Excluir Meta",
                "Tem certeza de que deseja excluir a meta \"" +
                        metaSelecionada.getNome() +
                        "\"?\n\nEsta ação não poderá ser desfeita."
        );

        if (!confirmou) {
            return;
        }

        try {
            metaService.excluir(metaSelecionada.getId());
            labelMensagem.setText("Meta excluída com sucesso.");
            carregarMetas();

        } catch (IllegalArgumentException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    private void configurarColunas() {
        colunaNome.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getNome()
                )
        );

        colunaObjetivo.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatoMoeda.format(dados.getValue().getValorObjetivo())
                )
        );

        colunaAtual.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        formatoMoeda.format(dados.getValue().getValorAtual())
                )
        );

        colunaProgresso.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().calcularProgresso() + "%"
                )
        );

        colunaPrazo.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getDataFim().format(formatoData)
                )
        );

        colunaStatus.setCellValueFactory(
                dados -> new SimpleStringProperty(
                        dados.getValue().getAtiva() ? "Ativa" : "Inativa"
                )
        );
    }

    private void carregarMetas() {
        tabelaMetas.setItems(
                FXCollections.observableArrayList(
                        metaService.listarTodas()
                )
        );
    }

}
