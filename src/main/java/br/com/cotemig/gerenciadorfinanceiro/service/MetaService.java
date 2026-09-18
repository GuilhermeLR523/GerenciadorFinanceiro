package br.com.cotemig.gerenciadorfinanceiro.service;

import br.com.cotemig.gerenciadorfinanceiro.model.Meta;
import br.com.cotemig.gerenciadorfinanceiro.repository.MetaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MetaService {

    private final MetaRepository metaRepository =
            new MetaRepository();

    public Meta cadastrar(
            String nome,
            BigDecimal valorObjetivo,
            LocalDate dataFim
    ) {
        validarNome(nome);
        validarValorObjetivo(valorObjetivo);
        validarDataFim(dataFim);

        Meta meta = new Meta(
                nome.trim(),
                valorObjetivo,
                dataFim
        );

        metaRepository.salvar(meta);
        return meta;
    }

    public Meta buscarPorId(Long id) {
        validarId(id);

        Meta meta = metaRepository.buscarPorId(id);

        if (meta == null) {
            throw new IllegalArgumentException("Meta não encontrada.");
        }

        return meta;
    }

    public List<Meta> listarTodas() {
        return metaRepository.listarTodas();
    }

    public List<Meta> listarAtivas() {
        return metaRepository.listarAtivas();
    }

    public void adicionarValor(Long id, BigDecimal valor) {
        validarValorAdicionado(valor);

        Meta meta = buscarPorId(id);

        if (!meta.getAtiva()) {
            throw new IllegalStateException(
                    "Não é possível adicionar valores a uma meta desativada."
            );
        }

        meta.adicionarValor(valor);
        metaRepository.atualizar(meta);
    }

    public BigDecimal calcularProgresso(Long id) {
        return buscarPorId(id).calcularProgresso();
    }

    public boolean verificarConclusao(Long id) {
        return buscarPorId(id).isConcluida();
    }

    public void desativar(Long id) {
        Meta meta = buscarPorId(id);

        if (!meta.getAtiva()) {
            throw new IllegalStateException("A meta já está desativada.");
        }

        meta.desativar();
        metaRepository.atualizar(meta);
    }

    public void ativar(Long id) {
        Meta meta = buscarPorId(id);

        if (meta.getAtiva()) {
            throw new IllegalStateException("A meta já está ativa.");
        }

        meta.ativar();
        metaRepository.atualizar(meta);
    }

    public void excluir(Long id) {
        Meta meta = buscarPorId(id);
        metaRepository.excluir(meta);
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da meta inválido.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da meta é obrigatório.");
        }

        if (nome.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "O nome da meta deve possuir no máximo 150 caracteres."
            );
        }
    }

    private void validarValorObjetivo(BigDecimal valorObjetivo) {
        if (valorObjetivo == null) {
            throw new IllegalArgumentException("O valor objetivo é obrigatório.");
        }

        if (valorObjetivo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor objetivo deve ser maior que zero."
            );
        }
    }

    private void validarValorAdicionado(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor é obrigatório.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }

    private void validarDataFim(LocalDate dataFim) {
        if (dataFim == null) {
            throw new IllegalArgumentException(
                    "A data final da meta é obrigatória."
            );
        }

        if (dataFim.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data final da meta não pode estar no passado."
            );
        }
    }
}
