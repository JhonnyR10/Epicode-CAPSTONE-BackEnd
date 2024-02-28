package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NoRankedLeagueException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.MiniSeries;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaLeague;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.LeagueEntryDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.MiniSeriesDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.SummonerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiotGamesApiService {
    private final WebClient webClient;
    @Value("${riot.token}")
    private String riotApiKey;

    public RiotGamesApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://euw1.api.riotgames.com").build();
    }

    public String getSummonerIdByUsername(String username) {
        String apiUrl = "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + username;

        SummonerDTO summonerDTO = webClient.get()
                .uri(apiUrl)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .block();

        assert summonerDTO != null;
        return summonerDTO.id();
    }

    public List<StatisticaLeague> getStatisticheGiocoById(String summonerId) {
        String apiUrl = "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + summonerId;

        List<LeagueEntryDTO> leagueEntryDTOs = webClient.get()
                .uri(apiUrl)
                .header("X-Riot-Token", riotApiKey)
                .retrieve()
                .bodyToFlux(LeagueEntryDTO.class)  // Deserializza la risposta come Flux di LeagueEntryDTO
                .collectList()
                .block();

        if (leagueEntryDTOs != null && !leagueEntryDTOs.isEmpty()) {
            return leagueEntryDTOs.stream()
                    .map(this::convertToStatisticaLeague)
                    .collect(Collectors.toList());
        } else {
            throw new NoRankedLeagueException("L'account non è in nessuna lega ranked.");
        }
    }

    private StatisticaLeague convertToStatisticaLeague(LeagueEntryDTO leagueEntryDTO) {
        StatisticaLeague statisticaLeague = new StatisticaLeague();
        statisticaLeague.setLeagueId(leagueEntryDTO.getLeagueId());
        statisticaLeague.setSummonerId(leagueEntryDTO.getSummonerId());
        statisticaLeague.setSummonerName(leagueEntryDTO.getSummonerName());
        statisticaLeague.setQueueType(leagueEntryDTO.getQueueType());
        statisticaLeague.setTier(leagueEntryDTO.getTier());
        statisticaLeague.setRank(leagueEntryDTO.getRank());
        statisticaLeague.setLeaguePoints(leagueEntryDTO.getLeaguePoints());
        statisticaLeague.setWins(leagueEntryDTO.getWins());
        statisticaLeague.setLosses(leagueEntryDTO.getLosses());
        statisticaLeague.setHotStreak(leagueEntryDTO.isHotStreak());
        statisticaLeague.setVeteran(leagueEntryDTO.isVeteran());
        statisticaLeague.setFreshBlood(leagueEntryDTO.isFreshBlood());
        statisticaLeague.setInactive(leagueEntryDTO.isInactive());

        // Gestisci MiniSeries se presente
        MiniSeriesDTO miniSeriesDTO = leagueEntryDTO.getMiniSeries();
        if (miniSeriesDTO != null) {
            MiniSeries miniSeries = new MiniSeries();
            miniSeries.setLosses(miniSeriesDTO.getLosses());
            miniSeries.setProgress(miniSeriesDTO.getProgress());
            miniSeries.setTarget(miniSeriesDTO.getTarget());
            miniSeries.setWins(miniSeriesDTO.getWins());
//            statisticaLeague.setMiniSeries(miniSeries);
        }

        return statisticaLeague;
    }
}
