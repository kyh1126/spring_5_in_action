package spring_7.api;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import spring_7.domain.Ingredient;
import spring_7.domain.Ingredient.Type;

public class IngredientResource extends RepresentationModel<IngredientResource> {

    @Getter
    private String name;

    @Getter
    private Type type;

    public IngredientResource(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }

}
