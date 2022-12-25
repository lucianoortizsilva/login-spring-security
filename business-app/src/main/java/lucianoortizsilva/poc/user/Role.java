package lucianoortizsilva.poc.user;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = -3547675638286920172L;


	private Long id;
	private String name;
	private List<Permission> permissions;
	public Role(final String name) {
		this.name = name;
	}

	public Role(final String name, final List<Permission> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

}