package ma.fstt.ecommerce1.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.ecommerce1.dao.*;
import ma.fstt.ecommerce1.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("commandLineC")
@SessionScoped
public class CommandLineC implements Serializable {

    @Inject
    private CommandLineDAO commandLineDAO;

    @Inject
    private ProductDAO productDAO;

    private List<CommandLineItem> commandLineItems = new ArrayList<>();
    private List<Product> products;

    @PostConstruct
    public void init() {
        System.out.println("✅ CommandLineC INITIALISÉ");
        initNewOrder();
    }

    public void initNewOrder() {
        this.commandLineItems = new ArrayList<>();
        this.commandLineItems.add(new CommandLineItem());
        reloadProducts();
    }

    // === MÉTHODES D'ACTION CORRIGÉES ===

    public void addProductRow() {
        System.out.println("✅ addProductRow APPELÉE - Avant: " + commandLineItems.size() + " lignes");
        commandLineItems.add(new CommandLineItem());
        reloadProducts();
        recalculateAll();
        System.out.println("✅ addProductRow TERMINÉE - Après: " + commandLineItems.size() + " lignes");
    }

    // CORRECTION : Prend l'index au lieu de l'objet
    public void removeProductRow(int index) {
        System.out.println("✅ removeProductRow APPELÉE pour index: " + index);
        if (index >= 0 && index < commandLineItems.size() && commandLineItems.size() > 1) {
            commandLineItems.remove(index);
            recalculateAll();
        }
    }

    // CORRECTION : Prend l'index en paramètre
    public void onProductChange(int index) {
        System.out.println("=== onProductChange pour index: " + index + " ===");
        if (index >= 0 && index < commandLineItems.size()) {
            CommandLineItem item = commandLineItems.get(index);
            System.out.println("Produit sélectionné: " +
                    (item.getProduct() != null ? item.getProduct().getName() : "null"));
            // Le sous-total est automatiquement recalculé via getSubtotal()
        }
        recalculateAll();
    }

    // CORRECTION : Prend l'index en paramètre
    public void onQuantityChange(int index) {
        System.out.println("=== onQuantityChange pour index: " + index + " ===");
        if (index >= 0 && index < commandLineItems.size()) {
            CommandLineItem item = commandLineItems.get(index);

            // FORCER le recalcul en modifiant l'objet
            Integer newQuantity = item.getQuantity();
            item.setQuantity(newQuantity); // Cela va déclencher setQuantity() qui a les logs

            System.out.println("🔄 Quantité changée à: " + item.getQuantity() +
                    " pour produit: " + (item.getProduct() != null ? item.getProduct().getName() : "null"));

            // Recalculer explicitement le sous-total
            if (item.getProduct() != null) {
                Double newSubtotal = item.getProduct().getPrice() * item.getQuantity();
                System.out.println("🧮 Nouveau sous-total calculé: " + newSubtotal);
            }
        }
        recalculateAll();
    }

    public void recalculateAll() {
        System.out.println("=== RECALCUL TOUS LES TOTAUX ===");
        for (int i = 0; i < commandLineItems.size(); i++) {
            CommandLineItem item = commandLineItems.get(i);
            System.out.println("Ligne " + i + " - Produit: " +
                    (item.getProduct() != null ? item.getProduct().getName() : "null") +
                    ", Quantité: " + item.getQuantity() +
                    ", Prix: " + (item.getProduct() != null ? item.getProduct().getPrice() : 0) +
                    ", Sous-total: " + item.getSubtotal());
        }
        System.out.println("Total général: " + calculateTotal());
    }

    public void deleteOrderLines(Long orderId) {
        commandLineDAO.deleteAllOrderlines(orderId);
    }

    public Double calculateTotal() {
        try {
            Double total = 0.0;
            for (CommandLineItem item : commandLineItems) {
                if (item.getProduct() != null && item.getQuantity() != null && item.getQuantity() > 0) {
                    total += item.getSubtotal();
                }
            }
            System.out.println("💰 Total calculé: " + total);
            return total;
        } catch (Exception e) {
            System.out.println("❌ Erreur calcul total: " + e.getMessage());
            return 0.0;
        }
    }

    public void reloadProducts() {
        this.products = productDAO.findAll();
        System.out.println("📦 Produits chargés: " + (products != null ? products.size() : 0));
    }

    public void saveCommandLines(Order order) {
        System.out.println("💾 Sauvegarde des lignes de commande pour order: " + order.getId_order());
        int savedLines = 0;
        for (CommandLineItem item : commandLineItems) {
            if (item.getProduct() != null && item.getQuantity() != null && item.getQuantity() > 0) {
                CommandLine commandLine = new CommandLine();
                commandLine.setOrder(order);
                commandLine.setProduct(item.getProduct());
                commandLine.setQuantity(item.getQuantity());
                commandLineDAO.create(commandLine);
                savedLines++;
                System.out.println("✅ Ligne sauvegardée: " + item.getProduct().getName() + " x" + item.getQuantity());
            }
        }
        System.out.println("💾 " + savedLines + " lignes sauvegardées");
    }


    // Ajoutez cette méthode pour gérer la sélection par ID
    public void onProductSelected(int index, Long productId) {
        System.out.println("🎯 onProductSelected - index: " + index + ", productId: " + productId);
        if (index >= 0 && index < commandLineItems.size() && productId != null) {
            Product selectedProduct = productDAO.findById(productId);
            commandLineItems.get(index).setProduct(selectedProduct);
            System.out.println("🎯 Produit associé: " + (selectedProduct != null ? selectedProduct.getName() : "null"));
            recalculateAll();
        }
    }

    // Ajoutez cette propriété pour stocker les IDs temporaires
    private List<Long> temporaryProductIds = new ArrayList<>();

    public List<Long> getTemporaryProductIds() {
        // Synchroniser avec commandLineItems
        while (temporaryProductIds.size() < commandLineItems.size()) {
            temporaryProductIds.add(null);
        }
        while (temporaryProductIds.size() > commandLineItems.size()) {
            temporaryProductIds.remove(temporaryProductIds.size() - 1);
        }
        return temporaryProductIds;
    }
    // = GETTERS/SETTERS ===
    public List<CommandLineItem> getCommandLineItems() {
        return commandLineItems;
    }

    public void setCommandLineItems(List<CommandLineItem> commandLineItems) {
        this.commandLineItems = commandLineItems;
    }

    public List<Product> getProducts() {
        if (products == null) {
            reloadProducts();
        }
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}