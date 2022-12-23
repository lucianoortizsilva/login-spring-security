package lucianoortizsilva.poc.config;

public interface ConfigurationToken {

	default String getSecret() {
		return "W%@VBrlgz#x@wfCwdk%2Gww^svc$AFBYskQ*H0m0ih@&igdNo*";
	}

	/**
	 * 
	 * @return milliseconds
	 * 
	 */
	default Long getExpiration() {
		return 120000L;
	}

}