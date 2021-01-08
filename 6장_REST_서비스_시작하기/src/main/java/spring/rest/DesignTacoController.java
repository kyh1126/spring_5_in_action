package spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.domain.Taco;
import spring.repository.jpa.TacoJpaRepository;

import java.util.List;
import java.util.Optional;

//@RestController
@RepositoryRestController
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DesignTacoController {
    private final TacoJpaRepository tacoRepository;

//    @GetMapping("/recent")
//    public CollectionModel<TacoModel> recentTacos() {
//        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//        List<Taco> tacos = tacoRepository.findAll(page).getContent();
//
//        CollectionModel<TacoModel> recentModels = new TacoModelAssembler().toCollectionModel(tacos);
//        recentModels.add(
//                WebMvcLinkBuilder.linkTo(
//                        WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacos()).withRel("recents"));
//        return recentModels;
//    }

    @GetMapping("/recent")
    public ResponseEntity<CollectionModel<TacoModel>> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepository.findAll(page).getContent();

        CollectionModel<TacoModel> recentModels = new TacoModelAssembler().toCollectionModel(tacos);
        recentModels.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacos()).withRel("recents"));
        return ResponseEntity.ok(recentModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepository.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepository.save(taco);
    }

    @Bean
    public RepresentationModelProcessor<PagedModel<Taco>> tacoProcessor(EntityLinks links) {
        return new RepresentationModelProcessor<PagedModel<Taco>>() {
            @Override
            public PagedModel<Taco> process(PagedModel<Taco> model) {
                model.add(links.linkFor(Taco.class).slash("recent").withRel("recents"));
                return model;
            }
        };
    }

}
