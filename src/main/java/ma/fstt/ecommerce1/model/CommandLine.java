package ma.fstt.ecommerce1.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor

@Entity(name = "CommandLineEntity")
@Table(name = "commandline")
public class CommandLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_commandline;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
