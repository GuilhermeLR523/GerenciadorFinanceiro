package br.com.cotemig.gerenciadorfinanceiro.service;

import br.com.cotemig.gerenciadorfinanceiro.model.Categoria;
import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.model.Transacao;
import br.com.cotemig.gerenciadorfinanceiro.repository.CategoriaRepository;
import br.com.cotemig.gerenciadorfinanceiro.repository.ContaRepository;
import br.com.cotemig.gerenciadorfinanceiro.repository.TransacaoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransacaoService {

    private final TransacaoRepository transacaoRepository =
            new TransacaoRepository();

    private final ContaRepository contaRepository =
            new ContaRepository();

    private final CategoriaRepository categoriaRepository =
            new CategoriaRepository();

    public Transacao cadastrar(
            String descricao,
            BigDecimal valor,
            TipoTransacao tipo,
            LocalDate data,
            String observacoes,
            Long contaId,
            Long categoriaId
    ) {
        validarDescricao(descricao);
        validarValor(valor);
        validarTipo(tipo);
        validarData(data);
        validarObservacoes(observacoes);

        Conta conta = buscarConta(contaId);
        Categoria categoria = buscarCategoria(categoriaId);

        validarContaAtiva(conta);
        validarCategoriaAtiva(categoria);
        validarCategoriaDaTransacao(tipo, categoria);

        Transacao transacao = new Transacao(
                descricao.trim(),
                valor,
                tipo,
                data,
                normalizarObservacoes(observacoes),
                conta,
                categoria
        );

        atualizarSaldoAoCadastrar(conta, tipo, valor);

        transacaoRepository.salvarComAtualizacaoDeSaldo(
                transacao,
                conta
        );

        return transacao;
    }

    public Transacao buscarPorId(Long id) {
        validarId(id);

        Transacao transacao = transacaoRepository.buscarPorId(id);

        if (transacao == null) {
            throw new IllegalArgumentException("Transação não encontrada.");
        }

        return transacao;
    }

    public List<Transacao> listarTodas() {
        return transacaoRepository.listarTodas();
    }

    public List<Transacao> listarPorTipo(TipoTransacao tipo) {
        validarTipo(tipo);
        return transacaoRepository.listarPorTipo(tipo);
    }

    public List<Transacao> listarPorPeriodo(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        if (dataInicial == null || dataFinal == null) {
            throw new IllegalArgumentException(
                    "As datas do período são obrigatórias."
            );
        }

        if (dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser posterior à data final."
            );
        }

        return transacaoRepository.listarPorPeriodo(
                dataInicial,
                dataFinal
        );
    }

    public void excluir(Long id) {
        Transacao transacao = buscarPorId(id);
        Conta conta = transacao.getConta();

        restaurarSaldoAoExcluir(
                conta,
                transacao.getTipo(),
                transacao.getValor()
        );

        transacaoRepository.excluirComAtualizacaoDeSaldo(
                transacao,
                conta
        );
    }

    private Conta buscarConta(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da conta inválido.");
        }

        Conta conta = contaRepository.buscarPorId(id);

        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        return conta;
    }

    private Categoria buscarCategoria(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido.");
        }

        Categoria categoria = categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada.");
        }

        return categoria;
    }

    private void atualizarSaldoAoCadastrar(
            Conta conta,
            TipoTransacao tipo,
            BigDecimal valor
    ) {
        if (tipo == TipoTransacao.RECEITA) {
            conta.adicionarSaldo(valor);
        } else {
            conta.removerSaldo(valor);
        }
    }

    private void restaurarSaldoAoExcluir(
            Conta conta,
            TipoTransacao tipo,
            BigDecimal valor
    ) {
        if (tipo == TipoTransacao.RECEITA) {
            conta.removerSaldo(valor);
        } else {
            conta.adicionarSaldo(valor);
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da transação inválido.");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException(
                    "A descrição da transação é obrigatória."
            );
        }

        if (descricao.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "A descrição deve possuir no máximo 150 caracteres."
            );
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException(
                    "O valor da transação é obrigatório."
            );
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor da transação deve ser maior que zero."
            );
        }
    }

    private void validarTipo(TipoTransacao tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da transação é obrigatório."
            );
        }
    }

    private void validarData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "A data da transação é obrigatória."
            );
        }
    }

    private void validarObservacoes(String observacoes) {
        if (observacoes != null && observacoes.trim().length() > 500) {
            throw new IllegalArgumentException(
                    "As observações devem possuir no máximo 500 caracteres."
            );
        }
    }

    private void validarContaAtiva(Conta conta) {
        if (!conta.getAtiva()) {
            throw new IllegalStateException(
                    "Não é possível registrar transações em uma conta desativada."
            );
        }
    }

    private void validarCategoriaAtiva(Categoria categoria) {
        if (!categoria.getAtiva()) {
            throw new IllegalStateException(
                    "Não é possível utilizar uma categoria desativada."
            );
        }
    }

    private void validarCategoriaDaTransacao(
            TipoTransacao tipo,
            Categoria categoria
    ) {
        if (categoria.getTipo() != tipo) {
            throw new IllegalArgumentException(
                    "A categoria selecionada não corresponde ao tipo da transação."
            );
        }
    }

    private String normalizarObservacoes(String observacoes) {
        if (observacoes == null || observacoes.isBlank()) {
            return null;
        }

        return observacoes.trim();
    }
}
