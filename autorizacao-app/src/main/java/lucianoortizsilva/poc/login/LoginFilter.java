package lucianoortizsilva.poc.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import lucianoortizsilva.poc.outh.OauthAuthorization;
import lucianoortizsilva.poc.outh.OauthAuthorizationService;
import lucianoortizsilva.poc.user.User;
import lucianoortizsilva.poc.user.UserDTO;
import lucianoortizsilva.poc.user.UserService;
import lucianoortizsilva.poc.user.token.TokenJwt;
import lucianoortizsilva.poc.util.JsonUtil;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private OauthAuthorizationService oauthAuthorizationService;
	private AuthenticationManager authenticationManager;
	private TokenJwt tokenJwt;
	private UserService userService;
	
	
	
	public LoginFilter(final AuthenticationManager authenticationManager, final UserService userService, final TokenJwt tokenJwt, final OauthAuthorizationService oauthAuthorizationService) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.oauthAuthorizationService = oauthAuthorizationService;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenJwt = tokenJwt;
	}
	
	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
		try {
			final UserDTO user = (UserDTO) JsonUtil.convertToObject(req.getInputStream(), UserDTO.class);
			log.info("Solicitando token para usuario com username: {}", user.getUsername());
			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userService.getUsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			return authenticationManager.authenticate(usernamePasswordAuthenticationToken);//valida a senha em AbstractUserDetailsAuthenticationProvider
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			final GeraErroBadRequest geraErroBadRequest = new GeraErroBadRequest(res);
			geraErroBadRequest.comMensagem(e.getMessage());
		}
		return null;
	}
	
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		final String username = ((User) authentication.getPrincipal()).getUsername();
		final User user = (User) this.userService.loadUserByUsername(username);
		final List<String> permissions = userService.getPermissions(user.getRoles());
		final List<GrantedAuthority> authorities = userService.getGrantedAuthorities(permissions);
		final List<String> authoritiesText = authorities.stream().map(granted -> granted.getAuthority()).collect(Collectors.toList());
		final String perfis = authoritiesText.stream().filter(authority-> authority.startsWith("ROLE")).map(role-> role.split("_")).map(role-> role[1]).reduce("", (x,y)-> (x + y));
		
		final Map<String, Object> infoToken = new HashMap<String, Object>();
		infoToken.put("username", username);
		infoToken.put("firstName", user.getFirstName());
		infoToken.put("lastName", user.getLastName());
		infoToken.put("perfis", perfis);
		infoToken.put("authorities", authoritiesText);
		
		final String token = this.tokenJwt.gerar(infoToken);
		final String authorization = "Bearer " + token;
		this.oauthAuthorizationService.save(new OauthAuthorization(authorization));
		response.setHeader(HttpHeaders.AUTHORIZATION, authorization);
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