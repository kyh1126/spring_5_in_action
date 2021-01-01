package spring.repository.jpa;


import org.springframework.data.repository.CrudRepository;
import spring.domain.Taco;

public interface TacoJpaRepository extends CrudRepository<Taco, Long> {
}
