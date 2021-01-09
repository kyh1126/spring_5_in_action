package spring_7.data;

import org.springframework.data.repository.CrudRepository;
import spring_7.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
