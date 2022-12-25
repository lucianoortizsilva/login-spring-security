package lucianoortizsilva.poc;

import java.util.Map;

import lucianoortizsilva.poc.core.NewTokenJWT;
import lucianoortizsilva.poc.core.PayloadService;
import lucianoortizsilva.poc.core.RefreshTokenJWT;
import lucianoortizsilva.poc.core.model.PayloadProperty;

//@formatter:off
public class Token {
	
	public String encode(final Map<String, Object> infoToken) {
		return new NewTokenJWT().create(infoToken);
	}
	
	public Map<PayloadProperty, Object> decode(final String authorization){
		return new PayloadService().getValue(authorization);
	}
	
	public String atualizar(final String authorization) {
		return new RefreshTokenJWT().update(authorization);
	}
	
}