package lucianoortizsilva.poc.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lucianoortizsilva.poc.jwt.Role;
import lucianoortizsilva.poc.jwt.RoleEnum;

@Getter
@ToString
@NoArgsConstructor
public class User implements UserDetails {

	private static final long serialVersionUID = -804917945275782212L;

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private Boolean enabled;
	private List<Role> roles;
	private Collection<? extends GrantedAuthority> authorities;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	public User(final String username, final String firstName, final String lastName, final String password,
			final Boolean enabled, final List<Role> roles) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	public boolean hasRole(final RoleEnum roleEnum) {
		return this.getAuthorities().contains(new SimpleGrantedAuthority(roleEnum.name()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}