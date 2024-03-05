package Giovanni.Longo.EpicodeCAPSTONEBackEnd.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public Customer createStripeCustomer(String email) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();

        return Customer.create(params);
    }

    public Customer retrieveStripeCustomer(String customerId) throws StripeException {
        return Customer.retrieve(customerId);
    }

    public String createCheckoutSession(Long userId, String stripeCustomerId, String successUrl, String cancelUrl) throws StripeException {
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setUnitAmount(799L) // 7.99 EUR in centesimi
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("VIP PASS")
                                .build())
                        .build())
                .build();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(stripeCustomerId)
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putMetadata("userId", userId.toString()) // Aggiunge metadata con l'ID dell'utente
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

}
