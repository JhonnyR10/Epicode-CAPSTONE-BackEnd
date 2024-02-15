package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("Fortnite")
public class StatisticaFortinite extends StatisticaGioco {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_overall")
    private StatisticaTipoGioco overall;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_solo")
    private StatisticaTipoGioco solo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_statistica_duo")
    private StatisticaTipoGioco duo;
}
