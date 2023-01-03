package lucianoortizsilva.poc.oauth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthAuthorizationRepository extends JpaRepository<OauthAuthorization, String> {}