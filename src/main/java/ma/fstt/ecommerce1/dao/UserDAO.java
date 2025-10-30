package ma.fstt.ecommerce1.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import ma.fstt.ecommerce1.model.User;
import ma.fstt.ecommerce1.utils.EntityManagerSingleton;

import java.util.List;

@Named
@RequestScoped
public class UserDAO {

    @Inject
    private EntityManagerSingleton ems;

    public User findById(Long id){
        EntityManager em = ems.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User create(User user) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }



    public void delete(Long id) {
        EntityManager em = ems.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserEntity u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<User> findByRole(String role) {
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.role = :role", User.class)
                    .setParameter("role", role)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public User authenticate(String email, String password){
        EntityManager em = ems.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email AND u.password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
