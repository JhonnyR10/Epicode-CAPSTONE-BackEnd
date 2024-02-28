package Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads;

public record SummonerDTO(String accountId,
                          int profileIconId,
                          long revisionDate,
                          String name,
                          String id,
                          String puuid,
                          long summonerLevel) {
}
