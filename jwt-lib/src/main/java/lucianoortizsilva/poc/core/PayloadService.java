package lucianoortizsilva.poc.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lucianoortizsilva.poc.config.ConfigurationToken;
import lucianoortizsilva.poc.core.model.PayloadProperty;
import lucianoortizsilva.poc.core.model.PayloadValue;
import lucianoortizsilva.poc.util.JsonUtil;

public class PayloadService implements ConfigurationToken {

	public Map<PayloadProperty, Object> getValue(final String authorization) {
		final Map<PayloadProperty, Object> map = getUsuarioFrom(authorization);
		validarDataHoraExpiracao((Long) map.get(PayloadProperty.DH_EXPIRATION));
		return map;
	}

	private Map<PayloadProperty, Object> getUsuarioFrom(final String authorization) {
		try {
			final byte[] secretKey = getSecret().getBytes();
			final String token = authorization.substring("Bearer".length()).trim();
			final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			final String json = JsonUtil.convertToJson(claims);
			final PayloadValue payload = (PayloadValue) JsonUtil.convertToObject(json, PayloadValue.class);
			final Map<PayloadProperty, Object> map = new HashMap<>();
			map.put(PayloadProperty.USERNAME, payload.getLogin());
			map.put(PayloadProperty.FIRSTNAME, payload.getFirstName());
			map.put(PayloadProperty.LASTNAME, payload.getLastName());
			map.put(PayloadProperty.AUTHORITIES, payload.getAuthorities());
			map.put(PayloadProperty.DH_EXPIRATION, payload.getExpiration());
			return map;
		} catch (final ExpiredJwtException e) {
			throw new TokenJwtException("Token Expirado");
		} catch (final Exception e) {
			throw new TokenJwtException("Token Inválido");
		}
	}

	private static void validarDataHoraExpiracao(final Long expiration) {
		final Date dhExpiration = new Date((long) expiration * 1000);
		if (dhExpiration.getTime() < new Date(System.currentTimeMillis()).getTime()) {
			throw new TokenJwtException("Token Expirado em: " + dhExpiration);
		}
	}

}