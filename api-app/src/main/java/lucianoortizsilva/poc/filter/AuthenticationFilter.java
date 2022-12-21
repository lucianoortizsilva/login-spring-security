package lucianoortizsilva.poc.filter;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import lucianoortizsilva.poc.error.GeraErroInesperado;
import lucianoortizsilva.poc.error.GeraErroNaoAutorizado;
import lucianoortizsilva.poc.error.GeraErroRequisicaoInvalida;
import lucianoortizsilva.poc.error.GeraErroSemPermissao;
import lucianoortizsilva.poc.token.TokenJwtException;
import lucianoortizsilva.poc.user.UserService;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.4.7/reference/html5/
 *
 */
//@formatter:off
@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Override
	public void doFilterInternal(HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
		final CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
		try {
			filterChain.doFilter(cachedBodyHttpServletRequest, response);
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				autenticar(cachedBodyHttpServletRequest, response, filterChain);
			} else if (e.getCause() instanceof SignatureException) {
				autenticar(cachedBodyHttpServletRequest, response, filterChain);
			} else if (e.getCause() instanceof ExpiredJwtException) {
				final GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem("Token Expirado");
			} else {
				log.error(e.getMessage(), e);
				final GeraErroInesperado geraErroInesperado = new GeraErroInesperado(response);
				geraErroInesperado.comMensagemPadrao();
			}
		}
	}

	private void autenticar(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) {
		try {
			final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (isEmpty(authorization) || !authorization.startsWith("Bearer")) {
				GeraErroRequisicaoInvalida geraErroRequisicaoInvalida = new GeraErroRequisicaoInvalida(response);
				geraErroRequisicaoInvalida.comMensagem("Authorization inválida");
			} else {
				final UsernamePasswordAuthenticationToken usuarioAutenticado = this.userService.getUsernamePasswordAuthenticationToken(authorization);
				if(usuarioAutenticado == null) {
					SecurityContextHolder.getContext().setAuthentication(null);
					filterChain.doFilter(request, response);
				} else {
					SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
					filterChain.doFilter(request, response);
				}
			}
		} catch (final UsernameNotFoundException e) {
			log.error(e.getMessage(), e);
			GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
			geraErroNaoAutorizado.comMensagem("Authorization com usuário não encontrado");
		} catch (final TokenJwtException e) {
			log.error(e.getMessage(), e);
			GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
			geraErroNaoAutorizado.comMensagem(e.getMessage());
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				log.error(e.getCause().getMessage(), e.getCause());
				GeraErroSemPermissao geraErroSemPermissao = new GeraErroSemPermissao(response);
				geraErroSemPermissao.comMensagem("Usuário sem permissão");
			} else {
				log.error(e.getMessage(), e);
				GeraErroInesperado geraErroInesperado = new GeraErroInesperado(response);
				geraErroInesperado.comMensagemPadrao();
			}
		}
	}

}