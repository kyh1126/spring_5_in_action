package spring_7.data;

import org.springframework.data.repository.CrudRepository;
import spring_7.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
