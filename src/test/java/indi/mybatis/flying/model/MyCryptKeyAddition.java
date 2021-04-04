package indi.mybatis.flying.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import indi.mybatis.flying.models.CryptKeyAdditional;

@Component
public class MyCryptKeyAddition implements CryptKeyAdditional {

	@Value("${cryptKeyAddition}")
	private String cryptKeyAddition;

	@Override
	public String getCryptKeyAddition() {
		return cryptKeyAddition;
	}

}
