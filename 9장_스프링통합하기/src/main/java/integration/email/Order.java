package integration.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
	private final String email;
	private List<Taco> tacos = new ArrayList<>();

	public void addTaco(Taco taco) {
		this.tacos.add(taco);
	}

	public Order(String email) {
		this.email = email;
	}

	@Data
	public class Taco {
		private String name;
		private List<String> ingredients;
	}
}
