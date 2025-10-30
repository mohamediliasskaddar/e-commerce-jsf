package ma.fstt.ecommerce1.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.fstt.ecommerce1.model.CommandLine;
import ma.fstt.ecommerce1.utils.EntityManagerSingleton;

import java.util.List;

@Named
@RequestScoped
public class CommandLineDAO {

    @Inject
    private EntityManagerSingleton ems;

    public CommandLine create(CommandLine cl) {
        EntityManager em = ems.getEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cl);
            tx.commit();
            return cl;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public CommandLine update(CommandLine cl) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CommandLine updated = em.merge(cl);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public void delete(CommandLine cl) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CommandLine managed = em.find(CommandLine.class, cl.getId_commandline());
            if (managed != null) {
                em.remove(managed);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public CommandLine findById(Long id) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.find(CommandLine.class, id);
        } finally {
            em.close();
        }
    }

    public List<CommandLine> findAllOrderlines(long order_id) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM CommandLineEntity c WHERE c.order.id_order = :order_id", CommandLine.class)
                    .setParameter("order_id", order_id)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteAllOrderlines(long order_id) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM CommandLineEntity c WHERE c.order.id_order = :order_id")
                    .setParameter("order_id", order_id)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
