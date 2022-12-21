package lucianoortizsilva.poc.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lucianoortizsilva.poc.outh.OauthAuthorization;
import lucianoortizsilva.poc.outh.OauthAuthorizationService;
import lucianoortizsilva.poc.token.Payload;
import lucianoortizsilva.poc.token.TokenJwt;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private TokenJwt tokenJwt;

	@Autowired
	private OauthAuthorizationService oauthAuthorizationService;

	
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return null;
	}

	
	
	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String authorization) {
		final Optional<OauthAuthorization> optional = oauthAuthorizationService.findByAuthorization(authorization);
		if (optional.isPresent()) {
			final Payload payload = (Payload) this.tokenJwt.getPayload(authorization);
			final List<String> permissions = payload.getAuthorities();
			final List<GrantedAuthority> authorities = getGrantedAuthorities(permissions);
			final User user = new User(payload.getLogin(), payload.getFirstName(), payload.getLastName(), null, Boolean.TRUE, null);
			return new UsernamePasswordAuthenticationToken(user, null, authorities);
		} else {
			return null;
		}
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String username, final String password) {
		return new UsernamePasswordAuthenticationToken(username, password);
	}
	
	
	
	public List<String> getPermissions(final List<Role> roles) {
		final List<String> permissionsAll = new ArrayList<>();
		final List<Permission> permissions = new ArrayList<>();

		for (final Role role : roles) {
			permissionsAll.add(role.getName());
			permissions.addAll(role.getPermissions());
		}
		for (final Permission permission : permissions) {
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