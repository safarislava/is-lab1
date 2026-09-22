package ru.ifmo.se.persistence.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@Transactional
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class TransactionalInterceptor {
    @Inject
    private EntityManager entityManager;

    @AroundInvoke
    @SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.SignatureDeclareThrowsException"})
    public Object manageTransaction(InvocationContext context) throws Exception {
        EntityTransaction tx = entityManager.getTransaction();
        boolean wasActive = tx.isActive();
        if (!wasActive) {
            tx.begin();
        }
        try {
            Object result = context.proceed();
            if (!wasActive && tx.isActive()) {
                tx.commit();
            }
            return result;
        } catch (Exception e) {
            if (!wasActive && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
