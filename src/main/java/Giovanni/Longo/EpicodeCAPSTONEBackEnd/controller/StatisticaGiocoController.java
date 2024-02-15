package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaGioco;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.StatisticaLeagueDTO;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.StatisticheGiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistiche")
public class StatisticaGiocoController {

    @Autowired
    private StatisticheGiocoService statisticheGiocoService;

    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<?> salvaStatisticaGiocoDaJson(@PathVariable Long userId, @RequestBody String jsonApiResponse) {
        try {
            statisticheGiocoService.salvaStatisticaGiocoDaJson(userId, jsonApiResponse);

            return ResponseEntity.ok("Statistica salvata con successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il salvataggio delle statistiche di gioco.");
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<String> aggiornaStatisticaGiocoDaJson(@PathVariable Long userId, @RequestParam Long statisticaId, @RequestBody String jsonApiResponse) {
        try {
            statisticheGiocoService.aggiornaStatisticaGiocoDaJson(userId, statisticaId, jsonApiResponse);
            return ResponseEntity.ok("Statistica aggiornata con successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il salvataggio delle statistiche di gioco.");
        }
    }


    @PostMapping("/lol/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<?> salvaStatisticaLol(@PathVariable Long userId, @RequestBody StatisticaLeagueDTO body) {
        try {
            statisticheGiocoService.salvaStatisticaLol(userId, body);

            return ResponseEntity.ok("Statistica salvata con successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il salvataggio delle statistiche di gioco.");
        }
    }

    @PutMapping("/lol/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<?> aggiornaStatisticaLol(@PathVariable Long userId, @RequestParam Long statisticaId, @RequestBody StatisticaLeagueDTO body) {
        try {
            statisticheGiocoService.aggiornaStatisticaLol(userId, statisticaId, body);

            return ResponseEntity.ok("Statistica aggiornata con successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il salvataggio delle statistiche di gioco.");
        }
    }

    @DeleteMapping("/{userId}/{statisticaId}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<String> eliminaStatisticaGioco(@PathVariable Long userId, @PathVariable Long statisticaId) {
        statisticheGiocoService.deleteStatisticaGioco(userId, statisticaId);
        return ResponseEntity.ok("Statistica eliminata con successo");
    }

    @GetMapping("/utente/{userId}/tutte")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<List<StatisticaGioco>> getTutteStatisticheUtente(@PathVariable Long userId) {
        List<StatisticaGioco> statisticheUtente = statisticheGiocoService.getTutteStatisticheUtente(userId);
        return ResponseEntity.ok(statisticheUtente);
    }

    @GetMapping("/utente/{userId}/{nomeGioco}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<List<StatisticaGioco>> getStatisticheUtentePerGioco(@PathVariable Long userId, @PathVariable String nomeGioco) {
        List<StatisticaGioco> statistichePerGioco = statisticheGiocoService.getStatisticheUtentePerGioco(userId, nomeGioco);
        return ResponseEntity.ok(statistichePerGioco);
    }

    @GetMapping("/gioco/{nomeGioco}")
    @PreAuthorize("hasAuthority('ADMIN') or (#userId == principal.id)")
    public ResponseEntity<List<StatisticaGioco>> getUtentiConStatistichePerGioco(@PathVariable String nomeGioco) {
        List<StatisticaGioco> utentiConStatistiche = statisticheGiocoService.getUtentiConStatistichePerGioco(nomeGioco);
        return ResponseEntity.ok(utentiConStatistiche);
    }
}