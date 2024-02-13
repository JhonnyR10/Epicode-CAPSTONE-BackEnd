package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.NotFoundException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaGioco;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaTipoGioco;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.StatisticaGiocoRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatisticheGiocoService {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // Aggiungi questo per abilitare il modulo JSR310
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Configurazione per evitare errori di serializzazione

    @Autowired
    private StatisticaGiocoRepository statisticaGiocoRepository;
    @Autowired
    private UserService userService;

    public StatisticaGioco findById(long id) {
        return statisticaGiocoRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void salvaStatisticaGiocoDaJson(Long id, String jsonApiResponse) {
        User found = userService.findById(id);
        if (found != null) {
            try {
                StatisticaGioco nuovaStatisticaGioco = new StatisticaGioco();
                nuovaStatisticaGioco.setUtente(found);
                JsonNode jsonNode = objectMapper.readTree(jsonApiResponse);

                // Aggiorna i campi specifici delle statistiche del gioco con i dati ottenuti dall'API
                nuovaStatisticaGioco.setNomeGioco("Fortnite");
                nuovaStatisticaGioco.setUsernameGioco(jsonNode.path("data").path("account").path("name").asText());
                nuovaStatisticaGioco.setOverall(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("overall")));
                nuovaStatisticaGioco.setSolo(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("solo")));
                nuovaStatisticaGioco.setDuo(mapToStatisticaTipoGioco(jsonNode.path("data").path("stats").path("all").path("duo")));

                // Altri aggiornamenti se necessario

                // Aggiorna il timestamp dell'ultimo aggiornamento
                nuovaStatisticaGioco.setUltimoAggiornamento(LocalDateTime.now());

                // Salva le statistiche aggiornate nel database
                StatisticaGioco savedStatisticaGioco = statisticaGiocoRepository.save(nuovaStatisticaGioco);
                // Aggiungi la nuova statistica alla lista di statistiche dell'utente
                found.getStatisticheGiochi().add(savedStatisticaGioco);


            } catch (Exception e) {
                e.printStackTrace();
                // Gestisci eventuali errori di deserializzazione o salvataggio
            }
        } else {
            // Gestisci il caso in cui l'utente non sia trovato
            System.out.println("Utente non trovato con ID: " + id);
        }
    }

    public void aggiornaStatisticaGiocoDaJson(Long userId, Long statisticaId, String jsonApiResponse) {
        User userFound = userService.findById(userId);
        if (userFound != null) {
            try {

                StatisticaGioco statisticaFound = this.findById(statisticaId);

                if (statisticaFound != null) {

                    JsonNode jsonNode = objectMapper.readTree(jsonApiResponse);

                    // Aggiorna i campi specifici delle statistiche del gioco con i dati ottenuti dall'API
                    statisticaFound.setNomeGioco("Fortnite");
                    statisticaFound.setUsernameGioco(jsonNode.path("data").path("account").path("name").asText());
                    updateStatisticaTipoGioco(statisticaFound.getOverall(), jsonNode.path("data").path("stats").path("all").path("overall"));
                    updateStatisticaTipoGioco(statisticaFound.getSolo(), jsonNode.path("data").path("stats").path("all").path("solo"));
                    updateStatisticaTipoGioco(statisticaFound.getDuo(), jsonNode.path("data").path("stats").path("all").path("duo"));

                    // Altri aggiornamenti se necessario

                    // Aggiorna il timestamp dell'ultimo aggiornamento
                    statisticaFound.setUltimoAggiornamento(LocalDateTime.now());

                    // Salva le statistiche aggiornate nel database
                    statisticaGiocoRepository.save(statisticaFound);


                } else {
                    // Gestisci il caso in cui la statistica non sia trovata
                    System.out.println("Statistica non trovata per il gioco: ");
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Gestisci eventuali errori di deserializzazione o salvataggio
            }
        } else {
            // Gestisci il caso in cui l'utente non sia trovato
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
            e.printStackTrace();  // o gestisci l'eccezione in modo appropriato
        }
    }

}