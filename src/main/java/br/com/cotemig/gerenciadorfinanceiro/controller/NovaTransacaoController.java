package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Categoria;
import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.service.CategoriaService;
import br.com.cotemig.gerenciadorfinanceiro.service.ContaService;
import br.com.cotemig.gerenciadorfinanceiro.service.TransacaoService;
import br.com.cotemig.gerenciadorfinanceiro.util.ValorUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovaTransacaoController {

    @FXML
    private TextField campoDescricao;

    @FXML
    private ComboBox<TipoTransacao> comboTipo;

    @FXML
    private TextField campoValor;

    @FXML
    private DatePicker campoData;

    @FXML
    private ComboBox<Conta> comboConta;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    @FXML
    private TextArea campoObservacoes;

    @FXML
    private Label labelMensagem;

    private final ContaService contaService = new ContaService();
    private final CategoriaService categoriaService = new CategoriaService();
    private final TransacaoService transacaoService = new TransacaoService();

    @FXML
    public void initialize() {
        configurarTipos();
        configurarContas();
        configurarCategorias();
        campoData.setValue(LocalDate.now());
    }

    @FXML
    private void tipoAlterado() {
        TipoTransacao tipo = comboTipo.getValue();

        comboCategoria.getItems().clear();
        comboCategoria.setValue(null);

        if (tipo == null) {
            return;
        }

        comboCategoria.setItems(
                FXCollections.observableArrayList(
                        categoriaService.listarPorTipo(tipo)
                )
        );
    }

    @FXML
    private void cadastrar() {
        try {
            TipoTransacao tipo = comboTipo.getValue();
            Conta conta = comboConta.getValue();
            Categoria categoria = comboCategoria.getValue();

            if (tipo == null) {
                throw new IllegalArgumentException("Selecione o tipo da transação.");
            }

            if (conta == null) {
                throw new IllegalArgumentException("Selecione uma conta.");
            }

            if (categoria == null) {
                throw new IllegalArgumentException("Selecione uma categoria.");
            }

            BigDecimal valor =
                    ValorUtil.converterParaBigDecimal(
                            campoValor.getText()
                    );

            transacaoService.cadastrar(
                    campoDescricao.getText(),
                    valor,
                    tipo,
                    campoData.getValue(),
                    campoObservacoes.getText(),
                    conta.getId(),
                    categoria.getId()
            );

            fecharJanela();

        } catch (NumberFormatException e) {
            labelMensagem.setText("Informe um valor válido.");

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

    private void configurarContas() {
        comboConta.setItems(
                FXCollections.observableArrayList(
                        contaService.listarAtivas()
                )
        );

        comboConta.setConverter(
                new StringConverter<>() {
                    @Override
                    public String toString(Conta conta) {
                        return conta == null ? "" : conta.getNome();
                    }

                    @Override
                    public Conta fromString(String texto) {
                        return null;
                    }
                }
        );
    }

    private void configurarCategorias() {
        comboCategoria.setConverter(
                new StringConverter<>() {
                    @Override
                    public String toString(Categoria categoria) {
                        return categoria == null ? "" : categoria.getNome();
                    }

                    @Override
                    public Categoria fromString(String texto) {
                        return null;
                    }
                }
        );
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
        stage.close();
    }
}
