package integration.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

@Configuration
public class TacoOrderEmailIntegration {

	public IntegrationFlow tacoOrderEmailFlow(
			EmailProperties emailProperties,
			EmailTacoOrderTransFormer emailTacoOrderTransFormer,
			OrderSubmitMessageHandler orderSubmitMessageHandler) {
		return IntegrationFlows
				.from(Mail.imapInboundAdapter(emailProperties.getImapUrl()),
						e -> e.poller(
								Pollers.fixedDelay(emailProperties.getPollRate())))
				.transform(emailTacoOrderTransFormer)
				.handle(orderSubmitMessageHandler)
				.get();
	}
}
