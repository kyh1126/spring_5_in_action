package integration.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

public class BidirectGatewayConfig {

	@Bean
	public IntegrationFlow upperFlow(){
		return IntegrationFlows
				.from("inChannel")
				.<String, String>transform(String::toUpperCase)
				.channel("outChannel")
				.get();
	}
}
