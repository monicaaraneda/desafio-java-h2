package ms.entities;


import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;
    private String name;
    private String email;
    private String password;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<Phone> phones;
}