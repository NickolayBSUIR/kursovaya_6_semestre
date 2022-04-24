package thesis.webcryptoexchange.model;
import java.util.*;
import javax.persistence.*;
import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_currencies")
public class UserCurrency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double count = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}