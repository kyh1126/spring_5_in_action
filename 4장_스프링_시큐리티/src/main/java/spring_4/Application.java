package spring_4;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring_4.domain.Ingredient;
import spring_4.repository.jpa.IngredientJpaRepository;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner dataLoader(IngredientJpaRepository repository) {
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
}
