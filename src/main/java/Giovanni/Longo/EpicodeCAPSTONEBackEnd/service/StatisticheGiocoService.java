package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.BadRequestException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.*;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.StatisticaLeagueDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.StatisticaGiocoRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticheGiocoService {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // Aggiungi questo per abilitare il modulo JSR310
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Configurazione per evitare errori di serializzazione

    @Autowired
    private StatisticaGiocoRepository statisticaGiocoRepository;
    @Autowired
    private UserService userService;

    private static StatisticaLeague getStatisticaLeague(StatisticaLeagueDTO body) {
        StatisticaLeague nuovaLega = new StatisticaLeague();
        nuovaLega.setQueueType(body.queueType());
        nuovaLega.setSummonerName(body.summonerName());
        nuovaLega.setHotStreak(body.hotStreak());
        nuovaLega.setWins(body.wins());
        nuovaLega.setVeteran(body.veteran());
        nuovaLega.setLosses(body.losses());
        nuovaLega.setRank(body.rank());
        nuovaLega.setTier(body.tier());
        nuovaLega.setInactive(body.inactive());
        nuovaLega.setFreshBlood(body.freshBlood());
        nuovaLega.setLeagueId(body.leagueId());
        nuovaLega.setSummonerId(body.summonerId());
        nuovaLega.setLeaguePoints(body.leaguePoints());
        return nuovaLega;
    }

    public StatisticaGioco findById(long id) {
        return statisticaGiocoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public void salvaStatisticaLol(Long id, StatisticaLeagueDTO body) {
        User found = userService.findById(id);
        if (found != null) {
            try {
                StatisticaLol nuovaStatisticaLol = new StatisticaLol();
                nuovaStatisticaLol.setUtente(found);
                nuovaStatisticaLol.setNomeGioco("League of Legends");
                nuovaStatisticaLol.setUsernameGioco(body.summonerName());
                StatisticaLeague nuovaLega = getStatisticaLeague(body);
                nuovaStatisticaLol.setLeague(nuovaLega);
                nuovaStatisticaLol.setUltimoAggiornamento(LocalDateTime.now());
                StatisticaGioco savedStatisticaGioco = statisticaGiocoRepository.save(nuovaStatisticaLol);
                found.getStatisticheGiochi().add(savedStatisticaGioco);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new NotFoundException(id);
            System.out.println("Utente non trovato con ID: " + id);
        }
    }

    public void aggiornaStatisticaLol(Long id, Long statId, StatisticaLeagueDTO body) {
        User found = userService.findById(id);
        if (found != null) {
            StatisticaLol statisticaFound = (StatisticaLol) this.findById(statId);
            try {
                statisticaFound.setNomeGioco("League of Legends");
                statisticaFound.setUsernameGioco(body.summonerName());
                StatisticaLeague nuovaLega = statisticaFound.getLeague();
                nuovaLega.setQueueType(body.queueType());
                nuovaLega.setSummonerName(body.summonerName());
                nuovaLega.setHotStreak(body.hotStreak());
                nuovaLega.setWins(body.wins());
                nuovaLega.setVeteran(body.veteran());
                nuovaLega.setLosses(body.losses());
                nuovaLega.setRank(body.rank());
                nuovaLega.setTier(body.tier());
                nuovaLega.setInactive(body.inactive());
                nuovaLega.setFreshBlood(body.freshBlood());
                nuovaLega.setLeagueId(body.leagueId());
                nuovaLega.setSummonerId(body.summonerId());
                nuovaLega.setLeaguePoints(body.leaguePoints());
                statisticaFound.setUltimoAggiornamento(LocalDateTime.now());
                statisticaGiocoRepository.save(statisticaFound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new NotFoundException(id);
            System.out.println("Utente non trovato con ID: " + id);
        }
    }

    public void salvaStatisticaGiocoDaJson(Long id, String jsonApiResponse) {
        User found = userService.findById(id);
        if (found != null) {
            try {
                StatisticaFortinite nuovaStatisticaGioco = new StatisticaFortinite();
                nuovaStatisticaGioco.setUtente(found);
                JsonNode jsonNode = objectMapper.readTree(jsonApiResponse);
                nuovaStatisticaGioco.setNomeGioco("Fortnite");
                nuovaStatisticaGioco.setUsernameGioco(jsonNode.path("data").path("account").path("name").asText());
                nuovaStatisticaGioco.setOverall(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("overall")));
                nuovaStatisticaGioco.setSolo(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("solo")));
                nuovaStatisticaGioco.setDuo(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("duo")));
                nuovaStatisticaGioco.setUltimoAggiornamento(LocalDateTime.now());
                StatisticaGioco savedStatisticaGioco = statisticaGiocoRepository.save(nuovaStatisticaGioco);
                found.getStatisticheGiochi().add(savedStatisticaGioco);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Utente non trovato con ID: " + id);
        }
    }

    public void aggiornaStatisticaGiocoDaJson(Long userId, Long statisticaId, String jsonApiResponse) {
        User userFound = userService.findById(userId);
        if (userFound != null) {
            try {
                StatisticaFortinite statisticaFound = (StatisticaFortinite) this.findById(statisticaId);

                if (statisticaFound != null) {
                    JsonNode jsonNode = objectMapper.readTree(jsonApiResponse);
                    statisticaFound.setNomeGioco("Fortnite");
                    statisticaFound.setUsernameGioco(jsonNode.path("data").path("account").path("name").asText());
                    updateStatisticaTipoGioco(statisticaFound.getOverall(), jsonNode.path("data").path("stats").path("all").path("overall"));
                    updateStatisticaTipoGioco(statisticaFound.getSolo(), jsonNode.path("data").path("stats").path("all").path("solo"));
                    updateStatisticaTipoGioco(statisticaFound.getDuo(), jsonNode.path("data").path("stats").path("all").path("duo"));
                    statisticaFound.setUltimoAggiornamento(LocalDateTime.now());
                    statisticaGiocoRepository.save(statisticaFound);
                } else {
                    System.out.println("Statistica non trovata per il gioco: ");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Utente non trovato con ID: " + userId);
        }
    }

    private StatisticaTipoGioco mapToStatisticaTipoGioco(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, StatisticaTipoGioco.class);
    }

    private void updateStatisticaTipoGioco(StatisticaTipoGioco existingStatisticaTipoGioco, JsonNode jsonNode) {
        try {
            objectMapper.updateValue(existingStatisticaTipoGioco, jsonNode);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            new BadRequestException("Non è stato possibile aggiornare la statistica");
        }
    }

    public void deleteStatisticaGioco(Long userId, Long statisticaId) {
        User userFound = userService.findById(userId);
        if (userFound != null) {
            StatisticaGioco statisticaFound = this.findById(statisticaId);
            if (statisticaFound != null) {
                statisticaGiocoRepository.delete(statisticaFound);
            } else {
                System.out.println("Statistica non trovata per il gioco: " + statisticaId);
            }
        } else {
            System.out.println("Utente non trovato con ID: " + userId);
        }
    }

    public List<StatisticaGioco> getTutteStatisticheUtente(Long userId) {
        User foundUser = userService.findById(userId);
        if (foundUser != null) {
            return foundUser.getStatisticheGiochi();
        } else {
            throw new NotFoundException("Utente non trovato con ID: " + userId);
        }
    }

    public List<StatisticaGioco> getStatisticheUtentePerGioco(Long userId, String nomeGioco) {
        User foundUser = userService.findById(userId);
        if (foundUser != null) {
            return foundUser.getStatisticheGiochi().stream()
                    .filter(statisticaGioco -> statisticaGioco.getNomeGioco().equalsIgnoreCase(nomeGioco))
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Utente non trovato con ID: " + userId);
        }
    }

    public List<StatisticaGioco> getUtentiConStatistichePerGioco(String nomeGioco) {
        return statisticaGiocoRepository.findAllByNomeGioco(nomeGioco);
    }
}