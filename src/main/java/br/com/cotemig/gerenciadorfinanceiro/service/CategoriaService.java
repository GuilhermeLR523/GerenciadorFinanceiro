package br.com.cotemig.gerenciadorfinanceiro.service;

import br.com.cotemig.gerenciadorfinanceiro.model.Categoria;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.repository.CategoriaRepository;
import br.com.cotemig.gerenciadorfinanceiro.repository.TransacaoRepository;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository =
            new CategoriaRepository();

    private final TransacaoRepository transacaoRepository =
            new TransacaoRepository();

    public Categoria cadastrar(
            String nome,
            TipoTransacao tipo,
            String cor,
            String icone
    ) {
        validarNome(nome);
        validarTipo(tipo);

        String nomeTratado = nome.trim();

        if (categoriaRepository.existePorNomeETipo(nomeTratado, tipo)) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria com esse nome e tipo."
            );
        }

        validarCor(cor);
        validarIcone(icone);

        Categoria categoria = new Categoria(
                nomeTratado,
                tipo,
                normalizarTextoOpcional(cor),
                normalizarTextoOpcional(icone)
        );

        categoriaRepository.salvar(categoria);
        return categoria;
    }

    public Categoria buscarPorId(Long id) {
        validarId(id);

        Categoria categoria = categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada.");
        }

        return categoria;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }

    public List<Categoria> listarPorTipo(TipoTransacao tipo) {
        validarTipo(tipo);
        return categoriaRepository.listarPorTipo(tipo);
    }

    public void alterarNome(Long id, String novoNome) {
        validarNome(novoNome);

        Categoria categoria = buscarPorId(id);
        categoria.setNome(novoNome.trim());

        categoriaRepository.atualizar(categoria);
    }

    public void desativar(Long id) {
        Categoria categoria = buscarPorId(id);

        if (!categoria.getAtiva()) {
            throw new IllegalStateException("A categoria já está desativada.");
        }

        categoria.desativar();
        categoriaRepository.atualizar(categoria);
    }

    public void ativar(Long id) {
        Categoria categoria = buscarPorId(id);

        if (categoria.getAtiva()) {
            throw new IllegalStateException("A categoria já está ativa.");
        }

        categoria.ativar();
        categoriaRepository.atualizar(categoria);
    }

    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);

        if (transacaoRepository.existePorCategoria(id)) {
            throw new IllegalStateException(
                    "Não é possível excluir esta categoria porque existem transações vinculadas a ela."
            );
        }

        categoriaRepository.excluir(categoria);
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        if (nome.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "O nome da categoria deve possuir no máximo 100 caracteres."
            );
        }
    }

    private void validarTipo(TipoTransacao tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da categoria é obrigatório.");
        }
    }

    private void validarCor(String cor) {
        if (cor != null && cor.trim().length() > 20) {
            throw new IllegalArgumentException(
                    "A cor deve possuir no máximo 20 caracteres."
            );
        }
    }

    private void validarIcone(String icone) {
        if (icone != null && icone.trim().length() > 50) {
            throw new IllegalArgumentException(
                    "O ícone deve possuir no máximo 50 caracteres."
            );
        }
    }

    private String normalizarTextoOpcional(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim();
    }
}
