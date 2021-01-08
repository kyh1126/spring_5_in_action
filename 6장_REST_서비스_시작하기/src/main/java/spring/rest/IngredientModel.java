package spring.rest;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import spring.domain.Ingredient;

import static spring.domain.Ingredient.Type;

@Relation(value = "ingredient", collectionRelation = "ingredients")
public class IngredientModel extends RepresentationModel<IngredientModel> {

    @Getter
    private final String name;

    @Getter
    private final Type type;

    public IngredientModel(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
