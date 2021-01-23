package webflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import webflux.domain.Ingredient;
import webflux.exception.UnknownIngredientException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final WebClient webClient;

    public Mono<Ingredient> getIngredientId(String ingredientId) {
        return webClient.get().uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.just(new UnknownIngredientException()))
                .bodyToMono(Ingredient.class);
    }

    public Mono<Ingredient> getIngredientIdWithTimeout(String ingredientId) {
        Mono<Ingredient> ingredient = webClient.get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
        ingredient.timeout(Duration.ofSeconds(1))
                .subscribe(i -> System.out.println(i),
                        e -> System.out.println(e));
        return ingredient;
    }

    public Mono<Ingredient> registerIngredient(Mono<Ingredient> ingredientMono) {
        return webClient.post()
                .uri("ingredients")
                .body(ingredientMono, Ingredient.class)
                .retrieve()
                .bodyToMono(Ingredient.class);
    }

    public Mono<Ingredient> registerIngredient(Ingredient ingredientMono) {
        return webClient.post()
                .uri("ingredients")
                .bodyValue(ingredientMono)
                .retrieve()
                .bodyToMono(Ingredient.class);
    }

    public Mono<Void> deleteIngredient(String ingredientId) {
        return webClient.delete()
                .uri("ingredients/id", ingredientId)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
