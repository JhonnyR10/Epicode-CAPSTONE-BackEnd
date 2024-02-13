package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.StatisticheGiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistiche")
public class StatisticaGiocoController {

    @Autowired
    private StatisticheGiocoService statisticheGiocoService;

    @PostMapping("/{userId}")
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
    public ResponseEntity<String> aggiornaStatisticaGiocoDaJson(@PathVariable Long userId, @RequestParam Long statisticaId, @RequestBody String jsonApiResponse) {
        statisticheGiocoService.aggiornaStatisticaGiocoDaJson(userId, statisticaId, jsonApiResponse);
        return ResponseEntity.ok("Statistica aggiornata con successo");
    }

    // Altri endpoint per gestire le operazioni sulle statistiche di gioco
}