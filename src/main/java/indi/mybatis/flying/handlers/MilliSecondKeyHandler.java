package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;

public class MilliSecondKeyHandler implements KeyHandler {

	private MilliSecondKeyHandler() {
	}

	private static class InnerInstance {
		private static final MilliSecondKeyHandler instance = new MilliSecondKeyHandler();
	}

	public static MilliSecondKeyHandler getInstance() {
		return InnerInstance.instance;
	}

	@Override
	public String getKey() {
		return new Long(System.currentTimeMillis()).toString();
	}

}
