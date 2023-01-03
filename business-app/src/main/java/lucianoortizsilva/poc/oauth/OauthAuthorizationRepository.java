package lucianoortizsilva.poc.oauth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("OauthAuthorizationRepository")
public interface OauthAuthorizationRepository extends JpaRepository<OauthAuthorization, String> {

	Optional<OauthAuthorization> findByAuthorization(String authorization);

}