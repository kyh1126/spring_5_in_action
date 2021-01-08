package spring_4.repository.jpa;


import org.springframework.data.repository.CrudRepository;
import spring_4.domain.Ingredient;

public interface IngredientJpaRepository extends CrudRepository<Ingredient, String> {
}
