package webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import webflux.domain.User;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<UserDetails> findByUsername(String username);

}

