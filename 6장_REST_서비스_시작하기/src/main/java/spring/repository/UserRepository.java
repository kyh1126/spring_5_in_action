package spring.repository;


import org.springframework.data.repository.CrudRepository;
import spring.domain.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
