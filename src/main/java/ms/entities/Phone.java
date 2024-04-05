package ms.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String citycode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
