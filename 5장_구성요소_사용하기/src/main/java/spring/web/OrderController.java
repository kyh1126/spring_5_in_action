package spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.domain.Order;
import spring.domain.user.User;
import spring.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import spring.repository.jpa.OrderJpaRepository;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

	private final OrderJpaRepository orderRepository;

	private OrderProps orderProps;

	public OrderController(OrderJpaRepository orderRepository, OrderProps orderProps) {
		this.orderRepository = orderRepository;
		this.orderProps = orderProps;
	}

	@GetMapping("/current")
	public String orderForm(@AuthenticationPrincipal User user,
	                        @ModelAttribute Order order) {
		if (order.getDeliveryName() == null) {
			order.setDeliveryName(user.getFullname());
		}
		if (order.getDeliveryState() == null) {
			order.setDeliveryStreet(user.getStreet());
		}
		if (order.getDeliveryCity() == null) {
			order.setDeliveryCity(user.getCity());
		}
		if (order.getDeliveryState() == null) {
			order.setDeliveryState(user.getState());
		}
		if (order.getDeliveryZip() == null) {
			order.setDeliveryZip(user.getZip());
		}
		return "orderForm";
	}

	@PostMapping
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
		if (errors.hasErrors()) {
			return "orderForm";
		}
		orderRepository.save(order);
		sessionStatus.setComplete();
		return "redirect:/";
	}

	@GetMapping
	public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
		Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
		model.addAttribute("orders",orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));
		return "orderList";
	}
}
