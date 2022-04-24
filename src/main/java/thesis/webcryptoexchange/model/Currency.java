package thesis.webcryptoexchange.model;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "currencies")
public class Currency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private Double rate;
    private Double change_hour;
    private Double change_day;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}