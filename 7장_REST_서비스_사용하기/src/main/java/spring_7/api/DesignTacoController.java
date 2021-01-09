//tag::recents[]
package spring_7.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring_7.data.TacoRepository;
import spring_7.domain.Taco;

@RestController
@RequestMapping(path = "/design", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DesignTacoController {
    private TacoRepository tacoRepository;

    public DesignTacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(page).getContent();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepository.save(taco);
    }

//    @GetMapping("/{id}")
//    public Taco tacoById(@PathVariable("id") Long id) {
//        Optional<Taco> optTaco = tacoRepository.findById(id);
//        if (optTaco.isPresent()) {
//            return optTaco.get();
//        }
//        return null;
//    }

//  @GetMapping("/{id}")
//  public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
//    Optional<Taco> optTaco = tacoRepo.findById(id);
//    if (optTaco.isPresent()) {
//      return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
//    }
//    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//  }

}

