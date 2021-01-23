package webflux.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class HelloRouterFunctionConfig {

    // RequestPredicates 인자를 넘기는 방식
    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return RouterFunctions
                .route(RequestPredicates.GET("/hello"),
                        request -> ServerResponse.ok().body(Mono.just("Hello, World!"), String.class))
                .andRoute(RequestPredicates.GET("/hello2"),
                        request -> ServerResponse.ok().body(Mono.just("Hello2, World!"), String.class));
    }

    // builder 방식 + 람다
    @Bean
    public RouterFunction<?> byeRouterFunction() {
        return RouterFunctions
                .route()
                .GET("/bye", request -> ServerResponse.ok().body(Mono.just("Bye, World!"), String.class))
                .GET("/bye2", request -> ServerResponse.ok().body(Mono.just("Bye2, World!"), String.class))
                .build();
    }

    // builder 방식 + 메소드 참조
    @Bean
    public RouterFunction<?> seeyaRouterFunction() {
        return RouterFunctions
                .route()
                .GET("/seeya", this::seeya)
                .GET("/seeya2", this::seeya2)
                .build();
    }

    public Mono<ServerResponse> seeya(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("seeya, World!"), String.class);
    }

    public Mono<ServerResponse> seeya2(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("seeya2, World!"), String.class);
    }


}
