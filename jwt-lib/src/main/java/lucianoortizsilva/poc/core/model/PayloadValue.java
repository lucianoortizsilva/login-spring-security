package lucianoortizsilva.poc.core.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayloadValue {

	@SerializedName(value = "dhExpiration")
	private Long expiration;

	@SerializedName(value = "authorities")
	private List<String> authorities;

	@SerializedName("username")
	private String login;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

}