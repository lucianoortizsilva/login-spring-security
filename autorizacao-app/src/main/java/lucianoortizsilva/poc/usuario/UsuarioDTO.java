package lucianoortizsilva.poc.usuario;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 4050945595677032300L;

	private String username;
	private String password;

}