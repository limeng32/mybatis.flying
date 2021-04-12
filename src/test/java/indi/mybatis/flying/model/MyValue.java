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

	private String[] warn;

	private String[] info;

	private String[] debug;

	private String[] trace;

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

	public String[] getWarn() {
		return warn;
	}

	public void setWarn(String[] warn) {
		this.warn = warn;
	}

	public String[] getInfo() {
		return info;
	}

	public void setInfo(String[] info) {
		this.info = info;
	}

	public String[] getDebug() {
		return debug;
	}

	public void setDebug(String[] debug) {
		this.debug = debug;
	}

	public String[] getTrace() {
		return trace;
	}

	public void setTrace(String[] trace) {
		this.trace = trace;
	}

}
