package ma.fstt.ecommerce1.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.fstt.ecommerce1.model.Product;
import ma.fstt.ecommerce1.utils.EntityManagerSingleton;

import java.util.List;

@Named
@RequestScoped
public class ProductDAO {

    @Inject
    private EntityManagerSingleton ems;

    public Product findById(Long id) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public Product create(Product product) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
            return product;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public Product update(Product product) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product updated = em.merge(product);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public void delete(Long productId) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, productId);
            if (product != null) {
                em.remove(product);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public List<Product> findAll() {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM ProductEntity p", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
