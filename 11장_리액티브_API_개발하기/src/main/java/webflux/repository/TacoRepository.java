package webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webflux.domain.Taco;

public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
