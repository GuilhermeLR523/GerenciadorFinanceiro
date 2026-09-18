package br.com.cotemig.gerenciadorfinanceiro.repository;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.model.Meta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MetaRepository {

    public void salvar(Meta meta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(meta);
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

    public Meta buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Meta.class, id);

        } finally {
            em.close();
        }
    }

    public List<Meta> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT m FROM Meta m ORDER BY m.dataFim",
                            Meta.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Meta> listarAtivas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            """
                            SELECT m
                            FROM Meta m
                            WHERE m.ativa = true
                            ORDER BY m.dataFim
                            """,
                            Meta.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Meta atualizar(Meta meta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Meta metaAtualizada = em.merge(meta);
            transaction.commit();
            return metaAtualizada;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public void excluir(Meta meta) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Meta metaGerenciada = em.find(
                    Meta.class,
                    meta.getId()
            );

            if (metaGerenciada != null) {
                em.remove(metaGerenciada);
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
}
