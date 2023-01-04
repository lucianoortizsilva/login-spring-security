package lucianoortizsilva.poc.autorizacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorizacaoService {

	@Autowired
	private AutorizacaoRepository autorizacaoRepository;

	@Transactional("autorizacaoTransactionManager")
	public void save(final Autorizacao oauthAuthorization) {
		this.autorizacaoRepository.saveAndFlush(oauthAuthorization);
	}

	@Transactional("autorizacaoTransactionManager")
	public Optional<Autorizacao> findByAuthorization(final String oauthAuthorization) {
		return this.autorizacaoRepository.findByAuthorization(oauthAuthorization);
	}

	public List<Autorizacao> getAll() {
		return autorizacaoRepository.findAll();
	}

}