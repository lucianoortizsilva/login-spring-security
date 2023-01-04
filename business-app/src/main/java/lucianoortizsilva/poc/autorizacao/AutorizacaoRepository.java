package lucianoortizsilva.poc.autorizacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorizacaoRepository extends JpaRepository<Autorizacao, String> {

	Optional<Autorizacao> findByAuthorization(String authorization);

}