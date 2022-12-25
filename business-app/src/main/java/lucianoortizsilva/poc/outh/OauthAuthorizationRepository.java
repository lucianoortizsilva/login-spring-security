package lucianoortizsilva.poc.outh;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthAuthorizationRepository extends JpaRepository<OauthAuthorization, String> {
	
	Optional<OauthAuthorization> findByAuthorization(String authorization);
	
}