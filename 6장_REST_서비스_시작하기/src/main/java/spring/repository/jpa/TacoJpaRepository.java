package spring.repository.jpa;


import org.springframework.data.repository.PagingAndSortingRepository;
import spring.domain.Taco;

public interface TacoJpaRepository extends PagingAndSortingRepository<Taco, Long> {
}
