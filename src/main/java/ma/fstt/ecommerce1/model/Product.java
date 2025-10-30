package ma.fstt.ecommerce1.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ProductEntity")
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id_product;
    private String name;
    private double price;
}
