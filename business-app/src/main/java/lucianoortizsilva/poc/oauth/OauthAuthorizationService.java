package lucianoortizsilva.poc.oauth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OauthAuthorizationService {

	@Autowired
	private OauthAuthorizationRepository oauthAuthorizationRepository;

	@Transactional("oauthTransactionManager")
	public void save(final OauthAuthorization oauthAuthorization) {
		this.oauthAuthorizationRepository.saveAndFlush(oauthAuthorization);
	}

	@Transactional("oauthTransactionManager")
	public Optional<OauthAuthorization> findByAuthorization(final String oauthAuthorization) {
		return this.oauthAuthorizationRepository.findByAuthorization(oauthAuthorization);
	}

	public List<OauthAuthorization> getAll() {
		return oauthAuthorizationRepository.findAll();
	}

}