package lucianoortizsilva.poc.oauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "AUTORIZACAO", name = "auth_authorization")
public class OauthAuthorization implements Serializable {

	private static final long serialVersionUID = -3999514235058392995L;

	@Id
	@Column(name = "cd_authorization", unique = true, length = 461)
	private String authorization;

}