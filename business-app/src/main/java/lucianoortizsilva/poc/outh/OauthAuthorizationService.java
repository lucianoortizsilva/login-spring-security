package lucianoortizsilva.poc.outh;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthAuthorizationService {

	@Autowired
	private OauthAuthorizationRepository repository;

	public void save(final OauthAuthorization oauthAuthorization) {
		this.repository.saveAndFlush(oauthAuthorization);
	}

	public Optional<OauthAuthorization> findByAuthorization(final String oauthAuthorization) {
		return this.repository.findByAuthorization(oauthAuthorization);
	}

	public List<OauthAuthorization> getAll() {
		return repository.findAll();
	}

}