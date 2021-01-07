package spring.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "taco.order")
@Data
@Validated
public class OrderProps {
    @Max(value = 1000, message = "max pageSize cannot exceed 1000")
    private int pageSize = 20;
}
