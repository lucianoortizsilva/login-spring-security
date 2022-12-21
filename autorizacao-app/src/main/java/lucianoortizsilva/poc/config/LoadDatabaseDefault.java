package lucianoortizsilva.poc.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lucianoortizsilva.poc.user.Permission;
import lucianoortizsilva.poc.user.PermissionEnum;
import lucianoortizsilva.poc.user.PermissionRepository;
import lucianoortizsilva.poc.user.Role;
import lucianoortizsilva.poc.user.RoleEnum;
import lucianoortizsilva.poc.user.RoleRepository;
import lucianoortizsilva.poc.user.User;
import lucianoortizsilva.poc.user.UserRepository;

@Component
public class LoadDatabaseDefault implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		final Permission create = createPermissionIfNotFound(PermissionEnum.CREATE.name());
		final Permission read = createPermissionIfNotFound(PermissionEnum.READ.name());
		final Permission update = createPermissionIfNotFound(PermissionEnum.UPDATE.name());
		final Permission delete = createPermissionIfNotFound(PermissionEnum.DELETE.name());

		final Role roleChefao = createRoleIfNotFound(RoleEnum.ROLE_CHEFAO, Arrays.asList(create, read, update, delete));
		final Role rolePersonagens = createRoleIfNotFound(RoleEnum.ROLE_PERSONAGENS, Arrays.asList(read, update));
		final Role roleOutros = createRoleIfNotFound(RoleEnum.ROLE_OUTROS, Arrays.asList(read));

		createUserIfNotFound("bowser@supermario.com", "Big Boss", "Bowser", roleChefao);
		createUserIfNotFound("mario@supermario.com", "Super", "Mario", rolePersonagens);
		createUserIfNotFound("fantasma@supermario.com", "Fantasma", "", roleOutros);

	}

	@Transactional
	private Permission createPermissionIfNotFound(final String name) {
		Optional<Permission> permission = permissionRepository.findByName(name);
		if (permission.isEmpty()) {
			return permissionRepository.save(new Permission(name));
		} else {
			return null;
		}
	}

	@Transactional
	private Role createRoleIfNotFound(final RoleEnum roleEnum, final List<Permission> permissions) {
		Optional<Role> role = roleRepository.findByName(roleEnum.name());
		if (role.isEmpty()) {
			return roleRepository.save(new Role(roleEnum.name(), permissions));
		} else {
			return null;
		}
	}

	@Transactional
	private void createUserIfNotFound(final String username, final String firstName, final String lastName, final Role role) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			final String password = bCryptPasswordEncoder.encode("12345");
			userRepository.save(new User(username, firstName, lastName, password, true, Arrays.asList(role)));
		}
	}

}