package me.june.spring.repository.jpa;

import me.june.spring.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientJpaRepository extends CrudRepository<Ingredient, String> {
}
