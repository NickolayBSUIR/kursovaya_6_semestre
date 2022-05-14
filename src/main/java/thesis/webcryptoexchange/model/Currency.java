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
@Table(name = "currencies")
public class Currency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String name;
    private Double rate;
    private Double change_hour;
    private Double change_day;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserCurrency> userCurrencies = new ArrayList<UserCurrency>();
}