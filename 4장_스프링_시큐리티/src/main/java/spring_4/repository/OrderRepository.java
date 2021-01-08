package spring_4.repository;


import spring_4.domain.Order;

public interface OrderRepository {
    Order save(Order order);
}
