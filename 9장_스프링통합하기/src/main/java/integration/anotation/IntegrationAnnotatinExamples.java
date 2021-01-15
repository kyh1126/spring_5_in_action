package integration.anotation;

import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@IntegrationComponentScan
@MessagingGateway
public class IntegrationAnnotatinExamples {
}
