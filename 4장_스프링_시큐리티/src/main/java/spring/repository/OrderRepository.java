package spring.repository;


import spring.domain.Order;

public interface OrderRepository {
    Order save(Order order);
}
