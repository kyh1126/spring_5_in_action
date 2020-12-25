package me.june.spring.repository;

import me.june.spring.domain.Order;

public interface OrderRepository {
    Order save(Order order);
}
