package integration.email;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OrderSubmitMessageHandler implements GenericHandler<Order> {

	private final RestTemplate rest;
	private final ApiProperties apiProperties;

	@Override
	public Object handle(Order order, MessageHeaders headers) {
		rest.postForObject(apiProperties.getUrl(), order, String.class);
		return null;
	}

	@Data
	@ConfigurationProperties(prefix = "tacocloud.api")
	@Component
	public class ApiProperties {
		private String url;
	}
}
