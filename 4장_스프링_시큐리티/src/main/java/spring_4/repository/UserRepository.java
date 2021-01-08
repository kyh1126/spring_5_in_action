package spring_4.repository;


import org.springframework.data.repository.CrudRepository;
import spring_4.domain.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
