package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class StatisticaGioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatisticaGioco;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private User utente;

    private String nomeGioco;

    private String usernameGioco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_overall")
    private StatisticaTipoGioco overall;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_solo")
    private StatisticaTipoGioco solo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_duo")
    private StatisticaTipoGioco duo;

    private LocalDateTime ultimoAggiornamento;
}
