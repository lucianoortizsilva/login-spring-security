package lucianoortizsilva.poc.user;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {

	private static final long serialVersionUID = -2865488382272483179L;

	private Long id;

	private String name;

	public Permission(final String name) {
		this.name = name;
	}

}