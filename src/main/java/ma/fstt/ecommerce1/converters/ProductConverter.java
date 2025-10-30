package ma.fstt.ecommerce1.converters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import ma.fstt.ecommerce1.dao.ProductDAO;
import ma.fstt.ecommerce1.model.Product;

@FacesConverter(value = "productConverter", managed = true)
@ApplicationScoped
public class ProductConverter implements Converter<Product> {

    @Inject
    private ProductDAO productDAO;

    @Override
    public Product getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("🔧 ProductConverter getAsObject: " + value);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            Product product = productDAO.findById(id);
            System.out.println("🔧 Produit trouvé: " + (product != null ? product.getName() : "null"));
            return product;
        } catch (NumberFormatException e) {
            System.out.println("❌ Erreur conversion ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Product value) {
        System.out.println("🔧 ProductConverter getAsString: " + (value != null ? value.getName() : "null"));
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId_product());
    }
}