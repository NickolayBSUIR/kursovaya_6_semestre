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
    private Long id;
    
    @Column(name = "currency_count")
    private Double currencyCount;
    @Column(name = "usd_count")
    private Double usdCount;
    private Boolean buying = true;
    private LocalDateTime time = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}