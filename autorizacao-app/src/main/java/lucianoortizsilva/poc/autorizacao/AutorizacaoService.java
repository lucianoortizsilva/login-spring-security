package lucianoortizsilva.poc.autorizacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorizacaoService {

	@Autowired
	private AutorizacaoRepository autorizacaoRepository;

	public void save(final Autorizacao oauthAuthorization) {
		this.autorizacaoRepository.saveAndFlush(oauthAuthorization);
	}

}