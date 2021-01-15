package integration.serviceactivator;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHandler;

import java.util.List;

public class ServiceActivatorExamples {

	@Bean
	@ServiceActivator(inputChannel = "someChannel")
	public MessageHandler sysoutHandler() {
		return System.out::println;
	}

	@Bean
	@ServiceActivator(inputChannel = "orderChannel", outputChannel = "completeChannel")
	public GenericHandler<Order> orderHandle() {
		return ((payload, headers) -> new Order(payload.id,payload.items));
	}

	//dsl로 구성하기
	public IntegrationFlow someFlow() {
		return IntegrationFlows
					.from("orderChannel")
				.handle(System.out::println)
				.get();
	}
	//서비스 액티베이터를 가장 마지막에 두지 않고, 중간에 사용하면
	//MessageHandler 말고 GenericHandler 사용해서 페이로드를 전달
	public IntegrationFlow orderFlow() {
		return IntegrationFlows
					.from("orderChannel")
				.<Order>handle(((payload, headers) -> new Order(payload.id,payload.items)))
				.get();
	}


	// tmp class
	public class Order {
		private Long id;
		private List<String> items;

		public Order(Long id, List<String> items) {
			this.id = id;
			this.items = items;
		}
	}


}
