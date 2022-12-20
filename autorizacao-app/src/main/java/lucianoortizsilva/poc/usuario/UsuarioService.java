package lucianoortizsilva.poc.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lucianoortizsilva.poc.token.Payload;
import lucianoortizsilva.poc.token.TokenJwt;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private TokenJwt tokenJwt;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não existe!"));
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String authorization) {
		final Payload payload = this.tokenJwt.getPayload(authorization);
		final Usuario user = (Usuario) this.loadUserByUsername(payload.getLogin());
		final List<String> permissions = getPermissions(user.getRoles());
		final List<GrantedAuthority> authorities = getGrantedAuthorities(permissions);
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String username, final String password) {
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	public List<String> getPermissions(final List<Perfil> roles) {
		final List<String> permissionsAll = new ArrayList<>();
		final List<Permissao> permissions = new ArrayList<>();
		for (final Perfil role : roles) {
			permissionsAll.add(role.getName());
			permissions.addAll(role.getPermissions());
		}
		for (final Permissao permission : permissions) {
			permissionsAll.add(permission.getName());
		}
		return permissionsAll;
	}

	public List<GrantedAuthority> getGrantedAuthorities(final List<String> permissions) {
		final List<GrantedAuthority> grantedAuthority = new ArrayList<>();
		for (final String permission : permissions) {
			grantedAuthority.add(new SimpleGrantedAuthority(permission));
		}
		return grantedAuthority;
	}

}