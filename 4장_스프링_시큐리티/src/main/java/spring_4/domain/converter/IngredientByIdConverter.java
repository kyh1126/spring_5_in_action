package spring_4.domain.converter;

import lombok.RequiredArgsConstructor;
import spring_4.domain.Ingredient;
import spring_4.repository.jpa.IngredientJpaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {

//    private final IngredientRepository ingredientRepository;
    private final IngredientJpaRepository ingredientJpaRepository;

    @Override
    public Ingredient convert(String id) {
//        return ingredientRepository.findById(id);
        return ingredientJpaRepository.findById(id).orElse(null);
    }
}
