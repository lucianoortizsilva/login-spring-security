package lucianoortizsilva.poc.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthAuthorizationService {

	@Autowired
	private OauthAuthorizationRepository repository;

	public void save(final OauthAuthorization oauthAuthorization) {
		this.repository.saveAndFlush(oauthAuthorization);
	}

}