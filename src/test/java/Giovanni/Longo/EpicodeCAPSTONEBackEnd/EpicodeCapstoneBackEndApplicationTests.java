package Giovanni.Longo.EpicodeCAPSTONEBackEnd;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.enums.Role;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.payloads.UserRegisterDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesRegex;

@SpringBootTest
class EpicodeCapstoneBackEndApplicationTests {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long utenteCreato = null;

    @Test
    void registraUtenteOK() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserRegisterDTO("test", "test", "password", "test@email.com", "test1", Role.ADMIN));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("http://localhost:3001/auth/register");

        response.then().assertThat().statusCode(201);
        response.then().assertThat().body(matchesRegex("\\{\"id\":.*\\}"));

        JsonNode jsonNode = objectMapper.readTree(response.body().asString());

        utenteCreato = Long.parseLong(jsonNode.get("id").toString());

    }

    @Test
    void loginOK() throws Exception {
        String requestBody = "{\"email\": \"test@email.com\",\"password\":\"password\"}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("http://localhost:3001/auth/login")
                .then()
                .statusCode(200);
//                .body(matchesRegex("\\{\"token\":\".{1,}\"\\}"));
    }

    @Test
    void loginNo() throws Exception {
        String requestBody = "{\"email\": \"email7@gmail.com\",\"password\":\"1\"}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("http://localhost:3001/auth/login")
                .then()
                .statusCode(401);

    }
}
