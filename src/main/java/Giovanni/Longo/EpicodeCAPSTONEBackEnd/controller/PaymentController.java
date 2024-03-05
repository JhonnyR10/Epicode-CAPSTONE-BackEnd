package Giovanni.Longo.EpicodeCAPSTONEBackEnd.controller;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository.UserRepository;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class PaymentController {
    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestParam Long userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        String stripeCustomerId = getOrCreateStripeCustomer(user);
//        String successUrl = "http://localhost:3000/success?session_id={CHECKOUT_SESSION_ID}";
        String cancelUrl = "http://localhost:3000/cancel";
        String successUrl = "http://localhost:3000/me";


        String sessionUrl = stripeService.createCheckoutSession(userId, stripeCustomerId, successUrl, cancelUrl);

        // Restituisci l'URL della sessione come oggetto JSON
        return ResponseEntity.ok(Map.of("url", sessionUrl));
    }

    private String getOrCreateStripeCustomer(User user) throws StripeException {
        if (user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty()) {
            return user.getStripeCustomerId();
        } else {
            Customer stripeCustomer = stripeService.createStripeCustomer(user.getEmail());
            user.setStripeCustomerId(stripeCustomer.getId());
            userRepository.save(user); // Salva l'ID del cliente di Stripe nell'utente
            return stripeCustomer.getId();
        }
    }
}
