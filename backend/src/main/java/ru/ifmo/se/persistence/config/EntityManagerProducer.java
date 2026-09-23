package ru.ifmo.se.persistence.config;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.persistence.jpa.PersistenceProvider;

import java.util.Collections;

@ApplicationScoped
public class EntityManagerProducer {
    private final EntityManagerFactory emf = new PersistenceProvider()
            .createEntityManagerFactory("is-lab1-pu", Collections.emptyMap());

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @PreDestroy
    public void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
