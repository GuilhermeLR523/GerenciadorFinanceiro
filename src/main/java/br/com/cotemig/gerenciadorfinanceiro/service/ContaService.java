package br.com.cotemig.gerenciadorfinanceiro.service;

import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoConta;
import br.com.cotemig.gerenciadorfinanceiro.repository.ContaRepository;
import br.com.cotemig.gerenciadorfinanceiro.repository.TransacaoRepository;

import java.math.BigDecimal;
import java.util.List;

public class ContaService {

    private final ContaRepository contaRepository =
            new ContaRepository();

    private final TransacaoRepository transacaoRepository =
            new TransacaoRepository();

    public Conta cadastrar(
            String nome,
            TipoConta tipo,
            BigDecimal saldoInicial
    ) {
        validarNome(nome);
        validarTipo(tipo);
        validarSaldoInicial(saldoInicial);

        String nomeTratado = nome.trim();

        if (contaRepository.existePorNome(nomeTratado)) {
            throw new IllegalArgumentException(
                    "Já existe uma conta com esse nome."
            );
        }

        Conta conta = new Conta(
                nomeTratado,
                tipo,
                saldoInicial
        );

        contaRepository.salvar(conta);
        return conta;
    }

    public Conta buscarPorId(Long id) {
        validarId(id);

        Conta conta = contaRepository.buscarPorId(id);

        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        return conta;
    }

    public List<Conta> listarTodas() {
        return contaRepository.listarTodas();
    }

    public List<Conta> listarAtivas() {
        return contaRepository.listarAtivas();
    }

    public void alterarNome(Long id, String novoNome) {
        validarNome(novoNome);

        Conta conta = buscarPorId(id);
        conta.setNome(novoNome.trim());

        contaRepository.atualizar(conta);
    }

    public void desativar(Long id) {
        Conta conta = buscarPorId(id);

        if (!conta.getAtiva()) {
            throw new IllegalStateException("A conta já está desativada.");
        }

        conta.desativar();
        contaRepository.atualizar(conta);
    }

    public void ativar(Long id) {
        Conta conta = buscarPorId(id);

        if (conta.getAtiva()) {
            throw new IllegalStateException("A conta já está ativa.");
        }

        conta.ativar();
        contaRepository.atualizar(conta);
    }

    public void excluir(Long id) {
        Conta conta = buscarPorId(id);

        if (transacaoRepository.existePorConta(id)) {
            throw new IllegalStateException(
                    "Não é possível excluir esta conta porque existem transações vinculadas a ela."
            );
        }

        contaRepository.excluir(conta);
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da conta inválido.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da conta é obrigatório.");
        }

        if (nome.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "O nome da conta deve possuir no máximo 100 caracteres."
            );
        }
    }

    private void validarTipo(TipoConta tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da conta é obrigatório.");
        }
    }

    private void validarSaldoInicial(BigDecimal saldoInicial) {
        if (saldoInicial == null) {
            throw new IllegalArgumentException("O saldo inicial é obrigatório.");
        }
    }
}
