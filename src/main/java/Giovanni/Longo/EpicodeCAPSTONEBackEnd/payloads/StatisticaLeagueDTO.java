package Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads;

import jakarta.validation.constraints.NotEmpty;

public record StatisticaLeagueDTO(@NotEmpty Long id,
                                  @NotEmpty String queueType,
                                  @NotEmpty String summonerName,
                                  @NotEmpty boolean hotStreak,
                                  @NotEmpty int wins,
                                  @NotEmpty boolean veteran,
                                  @NotEmpty int losses,
                                  @NotEmpty String rank,
                                  @NotEmpty String tier,
                                  @NotEmpty boolean inactive,
                                  @NotEmpty boolean freshBlood,
                                  @NotEmpty String leagueId,
                                  @NotEmpty String summonerId,
                                  @NotEmpty int leaguePoints) {
}
