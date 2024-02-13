package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class StatisticaTipoGioco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;
    private double scorePerMin;
    private double scorePerMatch;
    private int wins;
    private int top3;
    private int top5;
    private int top6;
    private int top10;
    private int top12;
    private int top25;
    private int kills;
    private double killsPerMin;
    private double killsPerMatch;
    private int deaths;
    private double kd;
    private int matches;
    private double winRate;
    private int minutesPlayed;
    private int playersOutlived;
    private LocalDateTime lastModified;

}
