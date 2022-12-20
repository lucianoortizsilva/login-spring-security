package lucianoortizsilva.poc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lucianoortizsilva.poc.login.LoginFilter;
import lucianoortizsilva.poc.token.TokenJwt;
import lucianoortizsilva.poc.usuario.UsuarioService;

/**
 * 
 * https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 * 
 * @author ortiz
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, order = 0, mode = AdviceMode.PROXY, proxyTargetClass = false)
public class WebSecurityConfigurerCustom extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private TokenJwt tokenJwt;

	@Autowired
	private UsuarioService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		permitirVisualizarTelaBancoH2(http);
		http.addFilter(new LoginFilter(authenticationManager(), this.userService, this.tokenJwt));
		http.cors().and().csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	private void permitirVisualizarTelaBancoH2(HttpSecurity http) throws Exception {
		if (Arrays.asList(this.env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}