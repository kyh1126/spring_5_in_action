package integration.email;

import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.mail.Message;

@Component
public class EmailTacoOrderTransFormer extends AbstractMailMessageTransformer<Order> {


	@Override
	protected AbstractIntegrationMessageBuilder<Order> doTransform(Message message) throws Exception {
//		Order tacoOrder = processPayload(message);
		return MessageBuilder.withPayload(new Order("mail"));
	}
}
