package Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
