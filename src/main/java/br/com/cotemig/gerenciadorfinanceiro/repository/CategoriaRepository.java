package br.com.cotemig.gerenciadorfinanceiro.repository;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.model.Categoria;
import br.com.cotemig.gerenciadorfinanceiro.model.TipoTransacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class CategoriaRepository {

    public void salvar(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(categoria);
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

    public Categoria buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Categoria.class, id);

        } finally {
            em.close();
        }
    }

    public List<Categoria> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT c FROM Categoria c ORDER BY c.nome",
                            Categoria.class
                    )
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Categoria> listarPorTipo(TipoTransacao tipo) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            """
                            SELECT c
                            FROM Categoria c
                            WHERE c.tipo = :tipo
                              AND c.ativa = true
                            ORDER BY c.nome
                            """,
                            Categoria.class
                    )
                    .setParameter("tipo", tipo)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Categoria atualizar(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Categoria categoriaAtualizada = em.merge(categoria);
            transaction.commit();
            return categoriaAtualizada;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public void excluir(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Categoria categoriaGerenciada = em.find(
                    Categoria.class,
                    categoria.getId()
            );

            if (categoriaGerenciada != null) {
                em.remove(categoriaGerenciada);
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

    public boolean existePorNomeETipo(
            String nome,
            TipoTransacao tipo
    ) {

        EntityManager entityManager = JPAUtil.getEntityManager();

        try {
            Long quantidade = entityManager.createQuery(
                            """
                            SELECT COUNT(c)
                            FROM Categoria c
                            WHERE LOWER(TRIM(c.nome)) = LOWER(TRIM(:nome))
                            AND c.tipo = :tipo
                            """,
                            Long.class
                    )
                    .setParameter("nome", nome)
                    .setParameter("tipo", tipo)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            entityManager.close();
        }
    }
}
