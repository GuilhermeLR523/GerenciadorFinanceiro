package br.com.cotemig.gerenciadorfinanceiro.config;

import br.com.cotemig.gerenciadorfinanceiro.util.DatabasePathUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            criarEntityManagerFactory();

    private JPAUtil() {
    }

    private static EntityManagerFactory criarEntityManagerFactory() {

        Map<String, Object> propriedades = new HashMap<>();

        propriedades.put(
                "jakarta.persistence.jdbc.url",
                DatabasePathUtil.getUrlJdbc()
        );

        return Persistence.createEntityManagerFactory(
                "gerenciadorFinanceiroPU",
                propriedades
        );
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void fechar() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}