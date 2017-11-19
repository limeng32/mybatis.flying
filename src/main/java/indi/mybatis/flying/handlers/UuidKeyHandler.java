package indi.mybatis.flying.handlers;

import java.util.UUID;

import indi.mybatis.flying.type.KeyHandler;

public class UuidKeyHandler implements KeyHandler {

	private UuidKeyHandler() {
	}

	private static class InnerInstance {
		private static final UuidKeyHandler instance = new UuidKeyHandler();
	}

	public static UuidKeyHandler getInstance() {
		return InnerInstance.instance;
	}

	@Override
	public String getKey() {
		return UUID.randomUUID().toString();
	}

}
