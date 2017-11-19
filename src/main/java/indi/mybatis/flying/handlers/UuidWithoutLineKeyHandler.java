package indi.mybatis.flying.handlers;

import java.util.UUID;

import indi.mybatis.flying.type.KeyHandler;

public class UuidWithoutLineKeyHandler implements KeyHandler {

	private UuidWithoutLineKeyHandler() {
	}

	private static class InnerInstance {
		private static final UuidWithoutLineKeyHandler instance = new UuidWithoutLineKeyHandler();
	}

	public static UuidWithoutLineKeyHandler getInstance() {
		return InnerInstance.instance;
	}

	@Override
	public String getKey() {
		return UUID.randomUUID().toString().replaceAll("\\-", "");
	}

}
