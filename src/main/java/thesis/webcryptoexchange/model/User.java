package thesis.webcryptoexchange.model;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    @Transient
    private String passwordConfirm;

    private String password;

    private String role = "USER";

    private Boolean ban = false;
}