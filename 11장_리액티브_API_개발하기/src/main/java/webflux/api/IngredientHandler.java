package webflux.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.domain.Ingredient;
import webflux.repository.IngredientRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RequiredArgsConstructor
public class IngredientHandler {

    private final IngredientRepository repository;

    public Mono<ServerResponse> list(ServerRequest request) {
        Flux<Ingredient> ingredients = repository.findAll();
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(ingredients, Ingredient.class);
    }

    public Mono<ServerResponse> getIngredient(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(ingredient -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(ingredient))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
