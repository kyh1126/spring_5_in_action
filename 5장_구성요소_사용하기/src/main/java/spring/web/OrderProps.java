package spring.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "taco.order")
@Data
@Validated
public class OrderProps {
    private int pageSize = 20;
}
