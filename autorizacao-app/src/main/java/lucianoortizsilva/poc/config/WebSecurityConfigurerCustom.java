package lucianoortizsilva.poc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lucianoortizsilva.poc.login.LoginFilter;
import lucianoortizsilva.poc.outh.OauthAuthorizationService;
import lucianoortizsilva.poc.user.UserService;
import lucianoortizsilva.poc.user.token.TokenJwt;

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
	private TokenJwt tokenJwt;

	@Autowired
	private UserService userService;

	@Autowired
	private OauthAuthorizationService oauthAuthorizationService;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().sameOrigin();
		httpSecurity.authorizeHttpRequests().antMatchers("/login").permitAll();
		httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll();
		httpSecurity.addFilter(new LoginFilter(authenticationManager(), this.userService, this.tokenJwt, this.oauthAuthorizationService));
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Arrays.asList("POST"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}