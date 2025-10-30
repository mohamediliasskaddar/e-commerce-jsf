package ma.fstt.ecommerce1.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.ecommerce1.dao.ProductDAO;
import ma.fstt.ecommerce1.model.Product;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProductC implements Serializable {

    @Inject
    private ProductDAO productDAO;

    private List<Product> products;
    private Product product = new Product();
    private Product selectedProduct;
    private String errorMessage;

    @PostConstruct
    public void init() {
        products = productDAO.findAll();
    }

    // Navigation vers la page d'ajout
    public String goToAdd() {
        this.product = new Product();
        this.errorMessage = null;
        return "/views/products/product-add?faces-redirect=true";
    }

    // Navigation vers la page d'édition
    public String goToEdit() {
        if (selectedProduct != null) {
            this.product = selectedProduct;
            this.errorMessage = null;
            return "/views/products/product-edit?faces-redirect=true";
        }
        return null;
    }

    // Ajouter un produit
    public String add() {
        try {
            if (isInvalid(product.getName(), String.valueOf(product.getPrice()))) {
                errorMessage = "Tous les champs sont obligatoires";
                return null;
            }

            productDAO.create(product);
            products = productDAO.findAll();
            return "/views/products/product-list?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de l'ajout: " + e.getMessage();
            return null;
        }
    }

    // Modifier un produit
    public String update() {
        try {
            if (isInvalid(product.getName(), String.valueOf(product.getPrice()))) {
                errorMessage = "Tous les champs sont obligatoires";
                return null;
            }

            productDAO.update(product);
            products = productDAO.findAll();
            return "/views/products/product-list?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de la modification: " + e.getMessage();
            return null;
        }
    }

    // Supprimer un produit
    public String delete() {
        try {
            if (selectedProduct != null) {
                productDAO.delete(selectedProduct.getId_product());
                products = productDAO.findAll();
            }
            return "/views/products/product-list?faces-redirect=true";

        } catch (Exception e) {
            errorMessage = "Erreur lors de la suppression: " + e.getMessage();
            return null;
        }
    }

    // Retour à la liste
    public String goToList() {
        products = productDAO.findAll();
        return "/views/products/product-list?faces-redirect=true";
    }

    // Validation helper
    private boolean isInvalid(String name, String price) {
        return (name == null || name.trim().isEmpty() ||
                price == null || price.trim().isEmpty());
    }

    // Getters and Setters
    public List<Product> getProducts() { return products; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}