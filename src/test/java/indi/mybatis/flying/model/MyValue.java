package indi.mybatis.flying.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyValue {

	@Value("${cryptKeyAddition:asd}")
	private String cryptKeyAddition;

	public String getCryptKeyAddition() {
		return cryptKeyAddition;
	}

	public void setCryptKeyAddition(String cryptKeyAddition) {
		this.cryptKeyAddition = cryptKeyAddition;
	}

}
