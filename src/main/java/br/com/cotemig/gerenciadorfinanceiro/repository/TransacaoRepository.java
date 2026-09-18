package br.com.cotemig.gerenciadorfinanceiro.repository;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import br.com.cotemig.gerenciadorfinanceiro.model.Transacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;

public class TransacaoRepository {

    public void salvarComAtualizacaoDeSaldo(
            Transacao transacao,
            Conta conta
    ) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(transacao);
            em.merge(conta);
            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public Transacao buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Transacao.class, id);

        } finally {
            em.close();
        }
    }

    public List<Transacao> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT t FROM Transacao t ORDER BY t.data DESC, t.id DESC",
                            Transacao.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Transacao> listarPorTipo(TipoTransacao tipo) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            """
                            SELECT t
                            FROM Transacao t
                            WHERE t.tipo = :tipo
                            ORDER BY t.data DESC, t.id DESC
                            """,
                            Transacao.class
                    )
                    .setParameter("tipo", tipo)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Transacao> listarPorPeriodo(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            """
                            SELECT t
                            FROM Transacao t
                            WHERE t.data BETWEEN :dataInicial AND :dataFinal
                            ORDER BY t.data DESC, t.id DESC
                            """,
                            Transacao.class
                    )
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Transacao atualizar(Transacao transacao) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Transacao transacaoAtualizada = em.merge(transacao);
            transaction.commit();
            return transacaoAtualizada;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public void excluirComAtualizacaoDeSaldo(
            Transacao transacao,
            Conta conta
    ) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Transacao transacaoGerenciada = em.merge(transacao);
            em.merge(conta);
            em.remove(transacaoGerenciada);

            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public boolean existePorConta(Long contaId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long quantidade = em.createQuery(
                            """
                            SELECT COUNT(t)
                            FROM Transacao t
                            WHERE t.conta.id = :contaId
                            """,
                            Long.class
                    )
                    .setParameter("contaId", contaId)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            em.close();
        }
    }

    public boolean existePorCategoria(Long categoriaId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long quantidade = em.createQuery(
                            """
                            SELECT COUNT(t)
                            FROM Transacao t
                            WHERE t.categoria.id = :categoriaId
                            """,
                            Long.class
                    )
                    .setParameter("categoriaId", categoriaId)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            em.close();
        }
    }
}
