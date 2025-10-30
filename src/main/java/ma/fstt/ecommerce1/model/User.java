package ma.fstt.ecommerce1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="UserEntity")
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;
    private String username;
    private String password;
    private String email;
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
