package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("League of Legends")
public class StatisticaLol extends StatisticaGioco {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_league")
    private StatisticaLeague league;
}
