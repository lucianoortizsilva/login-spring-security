package lucianoortizsilva.poc.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;
import lucianoortizsilva.poc.error.GeraErroBadRequest;
import lucianoortizsilva.poc.error.GeraErroNaoAutorizado;
import lucianoortizsilva.poc.token.TokenJwt;
import lucianoortizsilva.poc.usuario.Usuario;
import lucianoortizsilva.poc.usuario.UsuarioDTO;
import lucianoortizsilva.poc.usuario.UsuarioService;
import lucianoortizsilva.poc.util.JsonUtil;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private TokenJwt tokenJwt;

	private UsuarioService userService;

	public LoginFilter(final AuthenticationManager authenticationManager, final UsuarioService userService, final TokenJwt tokenJwt) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenJwt = tokenJwt;
	}
	
	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
		try {
			final UsuarioDTO user = (UsuarioDTO) JsonUtil.convertToObject(req.getInputStream(), UsuarioDTO.class);
			log.info("Solicitando token para usuario com username: {}", user.getUsername());
			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userService.getUsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			final GeraErroBadRequest geraErroBadRequest = new GeraErroBadRequest(res);
			geraErroBadRequest.comMensagem(e.getMessage());
		}
		return null;
	}
	
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		final String username = ((Usuario) authentication.getPrincipal()).getUsername();
		final Usuario user = (Usuario) this.userService.loadUserByUsername(username);
		final List<String> permissions = userService.getPermissions(user.getRoles());
		final List<GrantedAuthority> authorities = userService.getGrantedAuthorities(permissions);
		final String token = this.tokenJwt.generateToken(username, user.getFirstName(), user.getLastName(), authorities);
		response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		log.info("Authorization: Bearer {}", token);
	}

	
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			final GeraErroNaoAutorizado erroNaoAutorizado = new GeraErroNaoAutorizado(response);
			erroNaoAutorizado.comMensagem("Usuário e/ou senha inválidos");
		}
	}

}