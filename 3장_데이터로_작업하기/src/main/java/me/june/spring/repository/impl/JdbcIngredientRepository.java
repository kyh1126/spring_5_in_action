package me.june.spring.repository.impl;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Ingredient;
import me.june.spring.repository.IngredientRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class JdbcIngredientRepository implements IngredientRepository {

    private final JdbcTemplate jdbc;

// Spring 4.3 이후 부터 생략가능
// @Autowired
// Lombok 애노테이션 으로 생성자 생성
//    public JdbcIngredientRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//    }

    // JDBC 를 사용했다면 SQLException 을 처리하기 위한 try-catch 구문들이 모두 사라졌다..
    // 핵심은 ? -> JdbcTemplate 의 execute 메소드
    // SQLException 이 발생할 경우 DataAccessException (RuntimeException) 으로 변환해서 던져준다.
    // 이는 스프링의 철학 (PSA) 과도 관련이 있다. -> 데이터 액세스 기술의 종류와 상관없이 일관된 예외가 발생하도록 만들어준다.
    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query("select id, name, type from Ingredient", this::mapRowToIngredient);
    }

    @Override
    public Ingredient findById(String id) {
        return jdbc.queryForObject("select id, name, type from Ingredient where id = ?", this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update("insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type"))
        );
    }
}
