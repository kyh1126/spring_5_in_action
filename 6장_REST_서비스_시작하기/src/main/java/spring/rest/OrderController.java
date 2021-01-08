package spring.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.domain.Order;
import spring.repository.jpa.OrderJpaRepository;

@RestController
@RequestMapping(path = "/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderJpaRepository orderRepository;

    @PutMapping("/{orderId}")
    public Order putOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public Order patchOrder(@PathVariable("orderId") Long orderId, @RequestBody Order patch) {
        Order order = orderRepository.findById(orderId).get();

        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(order.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(order.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(order.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(order.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(order.getDeliveryZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(order.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(order.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(order.getCcCVV());
        }
        return orderRepository.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {
        }
    }

}
