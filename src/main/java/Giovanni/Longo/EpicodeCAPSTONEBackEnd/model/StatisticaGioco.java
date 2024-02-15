package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_gioco", discriminatorType = DiscriminatorType.STRING)
public abstract class StatisticaGioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatisticaGioco;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnoreProperties("statisticheGiochi")
    private User utente;

    private String nomeGioco;

    private String usernameGioco;


    private LocalDateTime ultimoAggiornamento;
}
