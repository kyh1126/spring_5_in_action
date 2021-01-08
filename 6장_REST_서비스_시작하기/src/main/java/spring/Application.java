package spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.domain.Ingredient;
import spring.domain.Taco;
import spring.repository.jpa.IngredientJpaRepository;
import spring.repository.jpa.TacoJpaRepository;

import java.util.List;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner ingredientDataLoader(IngredientJpaRepository repository) {
		return args -> {
			repository.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
			repository.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
			repository.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
			repository.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
			repository.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
			repository.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
			repository.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
			repository.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
			repository.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
			repository.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
		};
	}

	@Bean
	public ApplicationRunner tacoDataLoader(TacoJpaRepository repository) {
		return args -> {
			Taco taco = new Taco();
			taco.setName("wanda-taco");
			taco.setIngredients(List.of(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP)));
			repository.save(taco);
		};
	}
}
