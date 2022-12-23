package lucianoortizsilva.poc.core;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import lucianoortizsilva.poc.config.ConfigurationToken;

@Slf4j
public class RefreshTokenJWT implements ConfigurationToken {

	public String update(final String authorization) {
		final Date dhExpiration = new Date(System.currentTimeMillis() + getExpiration());
		Claims claims = getClaimsFromToken(authorization);
		claims.put("dhExpiration", dhExpiration);

		final byte[] secretKey = getSecret().getBytes();
		log.info("Date hora expiração renovada até: {}", dhExpiration);
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	private Claims getClaimsFromToken(final String fullToken) {
		final byte[] secretKey = getSecret().getBytes();
		final String token = fullToken.substring("Bearer".length()).trim();
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

}