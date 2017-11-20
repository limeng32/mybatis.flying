package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;

public class MySnowFlakeKeyHandler implements KeyHandler {

	@Override
	public String getKey() {
		return SnowFlakeKeyHandler.getInstance().getKey();
	}

}
