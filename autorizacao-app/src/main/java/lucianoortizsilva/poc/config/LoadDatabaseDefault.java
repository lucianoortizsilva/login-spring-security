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

import lucianoortizsilva.poc.usuario.Permissao;
import lucianoortizsilva.poc.usuario.PermissaoEnum;
import lucianoortizsilva.poc.usuario.PermissaoRepository;
import lucianoortizsilva.poc.usuario.Perfil;
import lucianoortizsilva.poc.usuario.PerfilEnum;
import lucianoortizsilva.poc.usuario.PerfilRepository;
import lucianoortizsilva.poc.usuario.Usuario;
import lucianoortizsilva.poc.usuario.UsuarioRepository;

@Component
public class LoadDatabaseDefault implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private PerfilRepository roleRepository;

	@Autowired
	private PermissaoRepository permissionRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		final Permissao create = createPermissionIfNotFound(PermissaoEnum.CREATE.name());
		final Permissao read = createPermissionIfNotFound(PermissaoEnum.READ.name());
		final Permissao update = createPermissionIfNotFound(PermissaoEnum.UPDATE.name());
		final Permissao delete = createPermissionIfNotFound(PermissaoEnum.DELETE.name());

		final Perfil perfilChefao = createRoleIfNotFound(PerfilEnum.ROLE_CHEFAO, Arrays.asList(create, read, update, delete));
		final Perfil perfilPersonagens = createRoleIfNotFound(PerfilEnum.ROLE_PERSONAGENS, Arrays.asList(read, update));
		final Perfil perfilOutros = createRoleIfNotFound(PerfilEnum.ROLE_OUTROS, Arrays.asList(read));

		createUserIfNotFound("bowser@supermario.com", "Big Boss", "Bowser", perfilChefao);
		createUserIfNotFound("funcionario@supermario.com", "Mariana", "Silva", perfilPersonagens);
		createUserIfNotFound("suporte@supermario.com", "Vanessa", "Silva", perfilOutros);

	}

	@Transactional
	private Permissao createPermissionIfNotFound(final String name) {
		Optional<Permissao> permission = permissionRepository.findByName(name);
		if (permission.isEmpty()) {
			return permissionRepository.save(new Permissao(name));
		} else {
			return null;
		}
	}

	@Transactional
	private Perfil createRoleIfNotFound(final PerfilEnum roleEnum, final List<Permissao> permissions) {
		Optional<Perfil> role = roleRepository.findByName(roleEnum.name());
		if (role.isEmpty()) {
			return roleRepository.save(new Perfil(roleEnum.name(), permissions));
		} else {
			return null;
		}
	}

	@Transactional
	private void createUserIfNotFound(final String username, final String firstName, final String lastName, final Perfil role) {
		Optional<Usuario> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			final String password = bCryptPasswordEncoder.encode("12345");
			userRepository.save(new Usuario(username, firstName, lastName, password, true, Arrays.asList(role)));
		}
	}

}