package ms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor  // Using Lombok to generate a no-argument constructor
@AllArgsConstructor // Lombok generates a constructor with all arguments
@Builder            // Lombok's builder pattern
@Entity
@Table(name = "phones")
@NamedQuery(name = "Phone.findByUserId.count", query = "SELECT COUNT(p) FROM Phone p WHERE p.user.id = :userId")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String citycode;

    @ManyToOne(fetch = FetchType.LAZY) // Improved handling with LAZY loading
    @JoinColumn(name = "user_id")
    private User user; // Maintain only the object reference

    // No need to separately map the 'user_id' field
}
