package me.june.spring.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.june.spring.domain.Order;
import me.june.spring.domain.Taco;
import me.june.spring.repository.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final SimpleJdbcInsert orderInserter;
    private final SimpleJdbcInsert orderTacoInserter;
    private final ObjectMapper objectMapper;

    public JdbcOrderRepository(JdbcTemplate jdbc, ObjectMapper objectMapper) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = objectMapper;
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();

        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }

        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());

        return orderInserter.executeAndReturnKey(values)
                .longValue();
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = Map.of("taco", orderId, "taco", taco.getId());
        orderTacoInserter.executeAndReturnKey(values);
    }
}
