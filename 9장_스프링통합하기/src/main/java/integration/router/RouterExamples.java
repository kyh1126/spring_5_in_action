package integration.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RouterExamples {
	@Bean
	@Router(inputChannel = "numberChannel")
	public AbstractMessageRouter evenOddRouter() {
		return new AbstractMessageRouter() {
			@Override
			protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
				Integer number = (Integer) message.getPayload();
				if (number % 2 == 0) {
					return Collections.singleton(evenChannel());
				}
				return Collections.singleton(oddChannel());
			}
		};
	}

	@Bean
	public MessageChannel oddChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel evenChannel() {
		return new DirectChannel();
	}

	//dsl 정의
	@Bean
	public IntegrationFlow numberRoutingFlow(Integer source) {
		return IntegrationFlows
				.from("numberChannel")
				.<Integer, String>route(n -> n%2 ==0 ? "Even" : "Odd",
						mapping -> mapping.subFlowMapping("Even",
								sf -> sf.<Integer,Integer>transform(n -> n*10))
				).get();
	}

}
