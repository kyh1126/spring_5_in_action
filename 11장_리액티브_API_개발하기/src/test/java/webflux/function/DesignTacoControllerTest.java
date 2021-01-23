package webflux.function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.api.DesignTacoController;
import webflux.domain.Ingredient;
import webflux.domain.Taco;
import webflux.repository.TacoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class DesignTacoControllerTest {

    @Test
    public void shouldReturnRecentTacos() {
        Taco[] tacos = {
                testTaco(1L), testTaco(2L),
                testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L),
                testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L),
                testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L),
                testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
        Mockito.when(tacoRepo.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepo)).build();

//        testClient.get().uri("/design/recent")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$").isArray()
//                .jsonPath("$").isNotEmpty()
//                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
//                .jsonPath("$[0].name").isEqualTo("Taco 1")
//                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
//                .jsonPath("$[1].name").isEqualTo("Taco 2")
//                .jsonPath("$[2].id").isEqualTo(tacos[1].getId().toString())
//                .jsonPath("$[2].name").isEqualTo("Taco 3")
//                .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
//                .jsonPath("$[11].name").isEqualTo("Taco 11")
//                .jsonPath("$[12]").doesNotExist();

//        ClassPathResource recentsResource = new ClassPathResource("/tacos/recent-tacos.json");
//        String recentsJson = StreamUtils.copyToString(recentsResource.getInputStream(), Charset.defaultCharset());
//        testClient.get().uri("/design/recent")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .json(recentsJson);

        testClient.get().uri("/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Taco.class)
                .contains(Arrays.copyOf(tacos, 12));
    }

    @Test
    public void shouldSaveATaco() {
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(1L));
        Taco savedTaco = testTaco(2L);
        Mono<Taco> savedTacoMono = Mono.just(savedTaco);

        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
        Mockito.when(tacoRepo.save(any())).thenReturn(savedTacoMono);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepo)).build();

        testClient.post()
                .uri("/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }


    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        taco.setId(number);
        taco.setName("Taco " + number);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredients(ingredients);
        return taco;
    }


}
