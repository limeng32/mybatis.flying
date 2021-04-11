package indi.mybatis.flying.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyValue {

	@Value("${cryptKeyAddition:asd}")
	private String cryptKeyAddition;

	@Value("${flying.logger.error:}")
	private String errorLogger;

	public String getCryptKeyAddition() {
		return cryptKeyAddition;
	}

	public void setCryptKeyAddition(String cryptKeyAddition) {
		this.cryptKeyAddition = cryptKeyAddition;
	}

	public String getErrorLogger() {
		return errorLogger;
	}

	public void setErrorLogger(String errorLogger) {
		this.errorLogger = errorLogger;
	}

}
