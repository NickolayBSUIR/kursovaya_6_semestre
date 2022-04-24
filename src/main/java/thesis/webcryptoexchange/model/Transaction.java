package thesis.webcryptoexchange.model;
import java.util.*;
import javax.persistence.*;
import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "transactions")
public class Transaction{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "currency_count")
    private Double currencyCount;
    @Column(name = "usd_count")
    private Double usdCount;
    private Boolean success = true;
    private LocalTime time = LocalTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}