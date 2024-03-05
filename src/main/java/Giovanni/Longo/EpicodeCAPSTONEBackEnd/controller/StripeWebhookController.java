package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StripeWebhookController {
    @Autowired
    private UserService userService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/stripe-webhook")
    public void handle(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        if (endpointSecret == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Webhook secret non configurato.");
        }

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verifica firma webhook fallita.");
        } catch (StripeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore Stripe durante l'elaborazione del webhook.");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getData().getObject();
            updateUserToVip(session);
        }
    }

    private void updateUserToVip(Session session) {
        String userId = session.getMetadata().get("userId");
        if (userId != null && !userId.isEmpty()) {
            try {
                System.out.println("Tentativo di attivare lo stato VIP per l'utente con ID: " + userId);
                userService.activateVipStatus(Long.parseLong(userId));
                System.out.println("Stato VIP attivato con successo per l'utente con ID: " + userId);
            } catch (NumberFormatException e) {
                System.err.println("Errore di formato numerico per l'ID utente: " + userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID utente non valido nei metadata.");
            } catch (Exception e) {
                System.err.println("Errore durante l'attivazione dello stato VIP per l'ID utente: " + userId);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante l'attivazione dello status VIP.");
            }
        } else {
            System.err.println("UserID non trovato nei metadata della sessione.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserID non trovato nei metadata della sessione.");
        }
    }
}
