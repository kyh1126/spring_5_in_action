package spring_7.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import spring_7.domain.Taco;

public interface TacoRepository extends PagingAndSortingRepository<Taco, Long> {
}
