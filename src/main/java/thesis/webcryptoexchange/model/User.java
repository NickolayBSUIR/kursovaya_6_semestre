package thesis.webcryptoexchange.model;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String password;
    private Double money = 1000.0;
    private String role = "ROLE_USER";
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserCurrency> userCurrencies = new ArrayList<UserCurrency>();
}