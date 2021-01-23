package webflux.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import webflux.domain.Taco;
import webflux.repository.TacoRepository;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@RequiredArgsConstructor
public class TacoRouterFunctionConfig {

    private final TacoRepository tacoRepository;

    @Bean
    public RouterFunction<?> routerFunction() {
        return RouterFunctions.route(GET("/design/taco"), this::recent)
                .andRoute(POST("/design"), this::postTaco);
    }

    private Mono<ServerResponse> postTaco(ServerRequest request) {
        Mono<Taco> taco = request.bodyToMono(Taco.class);
        Mono<Taco> savedTaco = tacoRepository.save(taco.block());
        return ServerResponse
                .created(URI.create("http:8080/design/taco/" + savedTaco.block().getId()))
                .body(savedTaco, Taco.class);
    }

    private Mono<ServerResponse> recent(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepository.findAll().take(12), Taco.class);
    }

}
