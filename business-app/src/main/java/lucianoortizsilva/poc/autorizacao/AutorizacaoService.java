package lucianoortizsilva.poc.autorizacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorizacaoService {

	@Autowired
	private AutorizacaoRepository oauthAuthorizationRepository;

	@Transactional("autorizacaoTransactionManager")
	public void save(final Autorizacao oauthAuthorization) {
		this.oauthAuthorizationRepository.saveAndFlush(oauthAuthorization);
	}

	@Transactional("autorizacaoTransactionManager")
	public Optional<Autorizacao> findByAuthorization(final String oauthAuthorization) {
		return this.oauthAuthorizationRepository.findByAuthorization(oauthAuthorization);
	}

	public List<Autorizacao> getAll() {
		return oauthAuthorizationRepository.findAll();
	}

}