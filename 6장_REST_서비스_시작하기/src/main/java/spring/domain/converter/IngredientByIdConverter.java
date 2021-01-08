package spring.domain.converter;

import lombok.RequiredArgsConstructor;
import spring.domain.Ingredient;
import spring.repository.jpa.IngredientJpaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final IngredientJpaRepository ingredientJpaRepository;

    @Override
    public Ingredient convert(String id) {
        return ingredientJpaRepository.findById(id).orElse(null);
    }
}
