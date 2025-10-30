package ma.fstt.ecommerce1.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.fstt.ecommerce1.model.Order;
import ma.fstt.ecommerce1.utils.EntityManagerSingleton;

import java.util.ArrayList;
import java.util.List;


@Named
@RequestScoped
public class OrderDAO {

    @Inject
    private EntityManagerSingleton ems;

    public Order findById(Long order_id) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.find(Order.class, order_id);
        } finally {
            em.close();
        }
    }

    public Order create(Order order) {
        System.out.println(">> Creating order: " + order);
        System.out.println(">> Total: " + order.getTotal());

        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(order);
            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public Order update(Order order) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Order updated = em.merge(order);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public void delete(Long orderId) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                em.remove(order);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    // CORRECTION : Utiliser OrderEntity comme nom d'entité
    public List<Order> findAll() {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT o FROM OrderEntity o", Order.class).getResultList();
        } finally {
            em.close();
        }
    }

    // CORRECTION : Utiliser OrderEntity comme nom d'entité
    public List<Order> findUserOrders(long userId) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT o FROM OrderEntity o WHERE o.user.id_user = :userId", Order.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // AJOUT : Méthode pour charger les commandes avec les détails
    public List<Order> findAllWithDetails() {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT o FROM OrderEntity o " +  // CORRECTION : OrderEntity
                                    "LEFT JOIN FETCH o.user " +
                                    "LEFT JOIN FETCH o.products  cl " +
                                    "LEFT JOIN FETCH cl.product " +
                                    "ORDER BY o.date DESC", Order.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    // AJOUT : Méthode pour charger les commandes utilisateur avec détails
    public List<Order> findUserOrdersWithDetails(long userId) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT o FROM OrderEntity o " +  // CORRECTION : OrderEntity
                                    "LEFT JOIN FETCH o.user " +
                                    "LEFT JOIN FETCH o.products cl " +
                                    "LEFT JOIN FETCH cl.product " +
                                    "WHERE o.user.id_user = :userId " +
                                    "ORDER BY o.date DESC", Order.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}