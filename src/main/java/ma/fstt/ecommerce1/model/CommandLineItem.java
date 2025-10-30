package ma.fstt.ecommerce1.model;

import java.io.Serializable;

public class CommandLineItem implements Serializable {
    private Product product;
    private Integer quantity = 1;

    // Constructeurs
    public CommandLineItem() {
        System.out.println("🆕 CommandLineItem créé");
    }

    public CommandLineItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters/Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        System.out.println("🎯 Produit défini: " + (product != null ? product.getName() : "null"));
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = (quantity != null && quantity > 0) ? quantity : 1;
        System.out.println("🔢 Quantité définie: " + this.quantity);
    }

    // Méthode de calcul
    public Double getSubtotal() {
        Double subtotal = 0.0;
//        if (product != null && quantity != null && product.getPrice() != null) {
            if (product != null && quantity != null) {

            subtotal = product.getPrice() * quantity;
        }
        System.out.println("🧮 Sous-total calculé: " + subtotal + " (Produit: " +
                (product != null ? product.getName() : "null") + ", Prix: " +
                (product != null ? product.getPrice() : 0) + ", Qté: " + quantity + ")");
        return subtotal;
    }

    @Override
    public String toString() {
        return "CommandLineItem{product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity + ", subtotal=" + getSubtotal() + "}";
    }
}