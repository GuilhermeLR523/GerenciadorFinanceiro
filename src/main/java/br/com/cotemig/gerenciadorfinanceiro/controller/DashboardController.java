package br.com.cotemig.gerenciadorfinanceiro.controller;

import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.model.Transacao;
import br.com.cotemig.gerenciadorfinanceiro.service.ContaService;
import br.com.cotemig.gerenciadorfinanceiro.service.MetaService;
import br.com.cotemig.gerenciadorfinanceiro.service.TransacaoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardController {

    private static final int LIMITE_TRANSACOES_RECENTES = 5;

    @FXML
    private Label labelSaldoTotal;

    @FXML
    private Label labelReceitas;

    @FXML
    private Label labelDespesas;

    @FXML
    private Label labelMetasAtivas;

    @FXML
    private TableView<Transacao> tabelaTransacoesRecentes;

    @FXML
    private TableColumn<Transacao, String> colunaData;

    @FXML
    private TableColumn<Transacao, String> colunaDescricao;

    @FXML
    private TableColumn<Transacao, String> colunaTipo;

    @FXML
    private TableColumn<Transacao, String> colunaConta;

    @FXML
    private TableColumn<Transacao, String> colunaValor;

    private final ContaService contaService = new ContaService();
    private final TransacaoService transacaoService = new TransacaoService();
    private final MetaService metaService = new MetaService();

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarTabela();
        carregarResumo();
        carregarTransacoesRecentes();
    }

    private void carregarResumo() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        BigDecimal saldoTotal = calcularSaldoTotal();
        BigDecimal receitas = calcularTotalMes(
                TipoTransacao.RECEITA,
                inicioMes,
                fimMes
        );
        BigDecimal despesas = calcularTotalMes(
                TipoTransacao.DESPESA,
                inicioMes,
                fimMes
        );

        int metasAtivas = metaService.listarAtivas().size();

        labelSaldoTotal.setText(formatoMoeda.format(saldoTotal));
        labelReceitas.setText(formatoMoeda.format(receitas));
        labelDespesas.setText(formatoMoeda.format(despesas));
        labelMetasAtivas.setText(String.valueOf(metasAtivas));
    }

    private void carregarTransacoesRecentes() {
        var recentes = transacaoService
                .listarTodas()
                .stream()
                .limit(LIMITE_TRANSACOES_RECENTES)
                .toList();

        tabelaTransacoesRecentes.setItems(
                FXCollections.observableArrayList(recentes)
        );
    }

    private void configurarTabela() {
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

    private BigDecimal calcularSaldoTotal() {
        return contaService
                .listarAtivas()
                .stream()
                .map(Conta::getSaldoAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalMes(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim
    ) {
        return transacaoService
                .listarPorPeriodo(inicio, fim)
                .stream()
                .filter(transacao -> transacao.getTipo() == tipo)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
