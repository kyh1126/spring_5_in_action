package me.june.spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.june.spring.domain.Ingredient;
import me.june.spring.domain.Order;
import me.june.spring.domain.Taco;
import me.june.spring.repository.IngredientRepository;
import me.june.spring.repository.TacoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@RequiredArgsConstructor
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;

    @ModelAttribute("order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute("taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);

        for (Ingredient.Type type : Ingredient.Type.values()) {
            model.addAttribute(type.toString().toLowerCase(), ingredients.stream().filter(x -> x.getType() == type).collect(Collectors.toList()));
        }

        model.addAttribute("taco", new Taco());

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco savedTaco = tacoRepository.save(design);
        order.addDesign(savedTaco);

        return "redirect:/orders/current";
    }
}
