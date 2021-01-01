package spring.repository.jpa;


import org.springframework.data.repository.CrudRepository;
import spring.domain.Ingredient;

public interface IngredientJpaRepository extends CrudRepository<Ingredient, String> {
}
