package webflux.function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.api.DesignTacoController;
import webflux.domain.Ingredient;
import webflux.domain.Taco;
import webflux.repository.TacoRepository;
import webflux.service.IngredientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ExceptionTest {


    @Test
    public void shouldReturnRecentTacos() {
        IngredientService service = Mockito.mock(IngredientService.class);
        service.getIngredientId("eeee").subscribe(i -> System.out.println(i));
    }


}
