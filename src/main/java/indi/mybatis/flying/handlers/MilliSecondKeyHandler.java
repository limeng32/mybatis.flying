package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;

public class MilliSecondKeyHandler implements KeyHandler {

	@Override
	public String getKey() {
		return new Long(System.currentTimeMillis()).toString();
	}

}
