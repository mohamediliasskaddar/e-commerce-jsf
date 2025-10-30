package ma.fstt.ecommerce1.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.ecommerce1.dao.UserDAO;
import ma.fstt.ecommerce1.model.User;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UserC implements Serializable {

    @Inject
    private UserDAO userDAO;

    private List<User> users;
    private User user = new User();
    private User selectedUser;
    private String errorMessage;

    @PostConstruct
    public void init() {
        users = userDAO.findAll();
    }
    private boolean showAddForm = false;

    // ... autres propriétés et méthodes

    public boolean isShowAddForm() {
        return showAddForm;
    }

    public void setShowAddForm(boolean showAddForm) {
        this.showAddForm = showAddForm;
    }

    // méthode pour activer l’affichage
    public void toggleAddForm() {
        this.showAddForm = !this.showAddForm;
    }
    public String add() {
        try {
            if (isInvalid(user.getUsername(), user.getEmail(), user.getPassword())) {
                errorMessage = "Tous les champs sont obligatoires";
                return null;
            }

            userDAO.create(user);
            users = userDAO.findAll();
            user = new User();
            return "/views/users/user-list?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de l'ajout: " + e.getMessage();
            return null;
        }
    }

    public String delete() {
        try {
            if (selectedUser != null) {
                userDAO.delete(selectedUser.getId_user());
                users = userDAO.findAll();
            }
            return "/views/users/user-list?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de la suppression: " + e.getMessage();
            return null;
        }
    }

    // Authentification (si nécessaire)
    public String login() {
        try {
            User authenticatedUser = userDAO.authenticate(user.getEmail(), user.getPassword());
            if (authenticatedUser != null) {
                // Stocker l'utilisateur en session
                this.user = authenticatedUser;
                return "/views/products/products?faces-redirect=true";
            } else {
                errorMessage = "Email ou mot de passe incorrect";
                return null;
            }
        } catch (Exception e) {
            errorMessage = "Erreur d'authentification: " + e.getMessage();
            return null;
        }
    }

    private boolean isInvalid(String username, String email, String password) {
        return (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty());
    }

    // Getters and Setters
    public List<User> getUsers() { return users; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public User getSelectedUser() { return selectedUser; }
    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}