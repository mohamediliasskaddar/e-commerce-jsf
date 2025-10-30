package ma.fstt.ecommerce1.controllers;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.ecommerce1.dao.OrderDAO;
import ma.fstt.ecommerce1.dao.UserDAO;
import ma.fstt.ecommerce1.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Named("orderC")
@SessionScoped
public class OrderC implements Serializable {
    @Inject
    private UserC userC;

    @Inject
    private OrderDAO orderDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private CommandLineC commandLineC;

    private List<Order> orders;
    private List<Order> userOrders;
    private Order selectedOrder;
    private String errorMessage;
    private Long selectedUserId;
    private List<Long> expandedOrderIds = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadAllOrders(); // Charger avec détails au démarrage
    }
    // === GESTION DE L'AFFICHAGE DES DÉTAILS ===
    public void toggleOrderDetails(Long orderId) {
        System.out.println("🔄 Toggle order details pour: " + orderId);

        if (expandedOrderIds.contains(orderId)) {
            expandedOrderIds.remove(orderId);
            System.out.println("📕 Détails masqués pour commande: " + orderId);
        } else {
            expandedOrderIds.add(orderId);
            System.out.println("📖 Détails affichés pour commande: " + orderId);
        }

        // Debug: afficher l'état actuel
        System.out.println("📋 Orders expanded: " + expandedOrderIds);
    }

    // Méthode spécifique pour le formulaire
    public void toggleOrderDetailsAction(Long orderId) {
        toggleOrderDetails(orderId);
    }

    public boolean isOrderExpanded(Long orderId) {
        boolean expanded = expandedOrderIds.contains(orderId);
        System.out.println("🔍 Order " + orderId + " expanded: " + expanded);
        return expanded;
    }

    // === CHARGEMENT DES COMMANDES AVEC DÉTAILS ===
    public void loadAllOrders() {
        try {
            orders = orderDAO.findAllWithDetails(); // Utiliser la méthode avec jointures
            System.out.println("📦 Commandes chargées avec détails: " + (orders != null ? orders.size() : 0));

            // Debug: vérifier si les lignes de commande sont chargées
            if (orders != null && !orders.isEmpty()) {
                for (Order order : orders) {
                    System.out.println("🔍 Commande #" + order.getId_order() +
                            " - Lignes: " + (order.getProducts() != null ? order.getProducts().size() : 0));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur chargement commandes: " + e.getMessage());
            e.printStackTrace();
            orders = new ArrayList<>();
        }
    }

    // === CHARGEMENT DES COMMANDES UTILISATEUR AVEC DÉTAILS ===
    public void loadUserOrders() {
        try {
            if (userC != null && userC.getUser() != null) {
                userOrders = orderDAO.findUserOrdersWithDetails(userC.getUser().getId_user());
                System.out.println("📦 Commandes utilisateur chargées avec détails: " +
                        (userOrders != null ? userOrders.size() : 0));

                // Debug
                if (userOrders != null && !userOrders.isEmpty()) {
                    for (Order order : userOrders) {
                        System.out.println("🔍 Ma Commande #" + order.getId_order() +
                                " - Lignes: " + (order.getProducts() != null ? order.getProducts().size() : 0));
                    }
                }
            } else {
                userOrders = new ArrayList<>();
                System.out.println("⚠️ Aucun utilisateur connecté pour charger les commandes");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur chargement commandes utilisateur: " + e.getMessage());
            e.printStackTrace();
            userOrders = new ArrayList<>();
        }
    }

    public String goToAllOrders() {
        loadAllOrders();
        return "/views/orders/orders?faces-redirect=true";
    }

    public String goToMyOrders() {
        loadUserOrders();
        return "/views/orders/my-orders?faces-redirect=true";
    }

//    // === GESTION DE L'AFFICHAGE DES DÉTAILS ===
//    public void toggleOrderDetails(Long orderId) {
//        if (expandedOrderIds.contains(orderId)) {
//            expandedOrderIds.remove(orderId);
//            System.out.println("📕 Détails masqués pour commande: " + orderId);
//        } else {
//            expandedOrderIds.add(orderId);
//            System.out.println("📖 Détails affichés pour commande: " + orderId);
//        }
//    }
//
//    public boolean isOrderExpanded(Long orderId) {
//        return expandedOrderIds.contains(orderId);
//    }

    // === CRÉATION DE COMMANDE ===
    public String goToCreateOrder() {
        commandLineC.initNewOrder();
        this.selectedUserId = null;
        this.errorMessage = null;
        return "/views/orders/order-add?faces-redirect=true";
    }

    // Créer la commande finale
    public String createOrder() {
        try {
            // Vérifier s'il y a au moins un produit valide
            boolean hasValidProducts = false;
            for (CommandLineItem item : commandLineC.getCommandLineItems()) {
                if (item.getProduct() != null && item.getQuantity() != null && item.getQuantity() > 0) {
                    hasValidProducts = true;
                    break;
                }
            }

            if (!hasValidProducts) {
                errorMessage = "Ajoutez au moins un produit valide à la commande";
                return null;
            }

            // DÉTERMINER L'UTILISATEUR AUTOMATIQUEMENT
            User user;

            if (userC != null && userC.getUser() != null && "CLIENT".equals(userC.getUser().getRole())) {
                user = userC.getUser();
                System.out.println("👤 Utilisateur client détecté automatiquement: " + user.getUsername());
            }
            else if (selectedUserId != null) {
                user = userDAO.findById(selectedUserId);
                if (user == null) {
                    errorMessage = "Client non trouvé";
                    return null;
                }
                System.out.println("👤 Utilisateur sélectionné par admin: " + user.getUsername());
            }
            else {
                errorMessage = "Veuillez sélectionner un client";
                return null;
            }

            // Créer la commande
            Order newOrder = new Order();
            newOrder.setDate(new Date());
            newOrder.setTotal(commandLineC.calculateTotal());
            newOrder.setUser(user);

            // Sauvegarder la commande
            orderDAO.create(newOrder);

            // Sauvegarder les lignes de commande
            commandLineC.saveCommandLines(newOrder);

            System.out.println("✅ Commande créée avec succès pour l'utilisateur: " + user.getUsername());

            // Réinitialiser et recharger
            commandLineC.initNewOrder();
            this.selectedUserId = null;
            this.errorMessage = null;
            loadAllOrders(); // Recharger la liste avec les nouveaux détails

            return "/views/orders/orders?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de la création de la commande: " + e.getMessage();
            e.printStackTrace();
            return null;
        }
    }

    // === SUPPRESSION ===
    public String deleteOrder() {
        try {
            if (selectedOrder != null) {
                commandLineC.deleteOrderLines(selectedOrder.getId_order());
                orderDAO.delete(selectedOrder.getId_order());
                loadAllOrders(); // Recharger après suppression
            }
            return "/views/orders/orders?faces-redirect=true";
        } catch (Exception e) {
            errorMessage = "Erreur lors de la suppression: " + e.getMessage();
            return null;
        }
    }

    // === CALCUL DU TOTAL POUR AFFICHAGE ===
    public Double calculateOrderTotal(Order order) {
        return order.getTotal();
    }

    // === GETTERS/SETTERS ===
    public List<Order> getOrders() {
        if (orders == null) {
            loadAllOrders();
        }
        return orders;
    }

    public List<Order> getUserOrders() {
        if (userOrders == null) {
            loadUserOrders();
        }
        return userOrders;
    }

    public Order getSelectedOrder() { return selectedOrder; }
    public void setSelectedOrder(Order selectedOrder) { this.selectedOrder = selectedOrder; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Long getSelectedUserId() { return selectedUserId; }
    public void setSelectedUserId(Long selectedUserId) { this.selectedUserId = selectedUserId; }
    public CommandLineC getCommandLineC() { return commandLineC; }
}