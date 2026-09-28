package ru.ifmo.se.persistence.config;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {
    @PersistenceUnit(unitName = "is-lab1-pu")
    private EntityManagerFactory emf;

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
}
