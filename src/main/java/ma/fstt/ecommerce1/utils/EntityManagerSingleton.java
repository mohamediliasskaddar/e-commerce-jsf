package ma.fstt.ecommerce1.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Named
@ApplicationScoped
public class EntityManagerSingleton {

    private EntityManagerFactory emf;

    public EntityManagerSingleton() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
