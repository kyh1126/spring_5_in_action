package webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webflux.domain.Ingredient;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, String> {
}
