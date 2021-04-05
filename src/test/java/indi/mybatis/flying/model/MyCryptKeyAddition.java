package indi.mybatis.flying.model;

import indi.mybatis.flying.models.CryptKeyAdditional;

public class MyCryptKeyAddition implements CryptKeyAdditional {

	@Override
	public String getCryptKeyAddition() {
		return ApplicationContextProvider.getBean(MyValue.class).getCryptKeyAddition();
	}

}
