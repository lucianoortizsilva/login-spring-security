package lucianoortizsilva.poc.core;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import lucianoortizsilva.poc.config.ConfigurationToken;

@Slf4j
public class NewTokenJWT implements ConfigurationToken {

	public String create(final Map<String, Object> infoToken) {
		final Date dhExpiration = new Date(System.currentTimeMillis() + getExpiration());
		infoToken.put("dhExpiration", dhExpiration);
		final byte[] secretKey = getSecret().getBytes();
		log.info("Date hora expiração: {}", dhExpiration);
		return Jwts.builder().setClaims(infoToken).signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

}