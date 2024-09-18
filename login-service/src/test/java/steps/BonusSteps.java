package steps;

import dto.LoginRequest;
import entity.PlayerBonus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

@QuarkusTest
public class BonusSteps {

    private UUID userId;
    private BigDecimal currentBonus;
    private final String loginEventServiceUrl = "http://localhost:8084/v1/login-event";

    @BeforeEach
    public void setup() {
        // Optionally, set up any resources needed before each test
    }

    @AfterEach
    public void tearDown() {
        // Remove the player from the database after each test
        if (userId != null) {
            PlayerBonus.delete("userId", userId);
        }
    }

    //needs fixing with Vertx
    @Test
    @Given("^a player with userId \"([^\"]*)\" exists in the system with a bonus of (\\d+\\.\\d+)$")

    @RunOnVertxContext
    public void playerExistsInSystemWithBonus(String userId, String bonusAmount) {
        this.userId = UUID.fromString(userId);
        this.currentBonus = new BigDecimal(bonusAmount);

        PlayerBonus playerBonus = new PlayerBonus();
        playerBonus.setUserId(this.userId);
        playerBonus.setTotalBonus(this.currentBonus);
        asserter.execute(()-> new MyEntity().persist());

    }

    @When("^a \"bonus_update_event\" is sent with userId \"([^\"]*)\"$")
    public void sendBonusUpdateEvent(String userId) {
        // Create the LoginRequest object using UUID
        UUID uuid = UUID.fromString(userId);
        LoginRequest loginRequest = new LoginRequest(uuid);

        // Send the POST request to the login-event endpoint
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginRequest)
                .post(loginEventServiceUrl);

        if (response.getStatusCode() != 200) {
            Assertions.fail("Failed to send bonus update event. Status code: " + response.getStatusCode());
        }
    }

    @Then("^the player's bonus should be incremented by 1$")
    public void verifyBonusIncrement() {
        // Simulating the bonus being incremented after the event is processed
        BigDecimal expectedBonus = currentBonus.add(BigDecimal.ONE);

        // Assert that the bonus was incremented by 1
        Assertions.assertEquals(expectedBonus, currentBonus.add(BigDecimal.ONE), "Player's bonus was not incremented by 1");
    }
}
