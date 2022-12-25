package lucianoortizsilva.poc.user.token;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lucianoortizsilva.poc.Token;
import lucianoortizsilva.poc.core.model.PayloadProperty;
import lucianoortizsilva.poc.core.model.PayloadValue;

//@formatter:off
@Component
public class TokenJwt {

	public String gerar(final Map<String, Object> infoToken) {
		return new Token().encode(infoToken);
	}

	@SuppressWarnings("unchecked")
	public PayloadValue getPayload(final String authorization) {
		final Map<PayloadProperty, Object> map = new Token().decode(authorization);
		final PayloadValue payload = new PayloadValue();
		payload.setLogin((String) map.get(PayloadProperty.USERNAME));
		payload.setAuthorities((List<String>) map.get(PayloadProperty.AUTHORITIES));
		payload.setExpiration((Long) map.get(PayloadProperty.DH_EXPIRATION));
		payload.setFirstName((String) map.get(PayloadProperty.FIRSTNAME));
		payload.setLastName((String) map.get(PayloadProperty.LASTNAME));
		return payload;
	}
	
	
}