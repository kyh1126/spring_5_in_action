package webflux.function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import webflux.api.DesignTacoController;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = DesignTacoController.class)
public class DesignTacoControllerWebTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void shouldReturnRecentTacos() throws IOException {
        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[?(@.id == 'TACO1')].name").isEqualTo("Carnivore")
                .jsonPath("$[?(@.id == 'TACO2')].name").isEqualTo("Bovine Bounty")
                .jsonPath("$[?(@.id == 'TACO3')].name").isEqualTo("Veg-Out");
    }

}