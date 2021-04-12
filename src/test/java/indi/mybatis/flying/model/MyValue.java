package indi.mybatis.flying.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flying.logger")
public class MyValue {

	@Value("${cryptKeyAddition:asd}")
	private String cryptKeyAddition;

	private String[] error;

	public String getCryptKeyAddition() {
		return cryptKeyAddition;
	}

	public void setCryptKeyAddition(String cryptKeyAddition) {
		this.cryptKeyAddition = cryptKeyAddition;
	}

	public String[] getError() {
		return error;
	}

	public void setError(String[] error) {
		this.error = error;
	}

}
