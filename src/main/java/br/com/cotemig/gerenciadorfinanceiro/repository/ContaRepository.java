package br.com.cotemig.gerenciadorfinanceiro.repository;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.model.Conta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ContaRepository {

    public void salvar(Conta conta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(conta);
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

    public Conta buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Conta.class, id);

        } finally {
            em.close();
        }
    }

    public List<Conta> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT c FROM Conta c ORDER BY c.nome",
                            Conta.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Conta> listarAtivas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT c FROM Conta c WHERE c.ativa = true ORDER BY c.nome",
                            Conta.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Conta atualizar(Conta conta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Conta contaAtualizada = em.merge(conta);
            transaction.commit();
            return contaAtualizada;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public void excluir(Conta conta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Conta contaGerenciada = em.find(
                    Conta.class,
                    conta.getId()
            );

            if (contaGerenciada != null) {
                em.remove(contaGerenciada);
            }

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

    public boolean existePorNome(String nome) {

        EntityManager entityManager = JPAUtil.getEntityManager();

        try {
            Long quantidade = entityManager.createQuery(
                            """
                            SELECT COUNT(c)
                            FROM Conta c
                            WHERE LOWER(TRIM(c.nome)) = LOWER(TRIM(:nome))
                            """,
                            Long.class
                    )
                    .setParameter("nome", nome)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            entityManager.close();
        }
    }
}
