package spring_4.repository.jpa;


import org.springframework.data.repository.CrudRepository;
import spring_4.domain.Taco;

public interface TacoJpaRepository extends CrudRepository<Taco, Long> {
}
