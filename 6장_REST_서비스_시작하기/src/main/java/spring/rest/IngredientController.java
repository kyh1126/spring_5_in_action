package spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.domain.Ingredient;
import spring.repository.jpa.IngredientJpaRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/ingredients", produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientJpaRepository ingredientRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> tacoById(@PathVariable("id") String id) {
        Optional<Ingredient> optTaco = ingredientRepository.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
