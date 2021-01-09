package spring_7.data;

import org.springframework.data.repository.CrudRepository;
import spring_7.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
