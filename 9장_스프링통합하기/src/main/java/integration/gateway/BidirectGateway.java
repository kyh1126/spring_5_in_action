package integration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "inChannel", defaultReplyChannel = "outChannel")
public interface BidirectGateway {
	String uppercase(String in);
	String toRoman(Integer in);
}
