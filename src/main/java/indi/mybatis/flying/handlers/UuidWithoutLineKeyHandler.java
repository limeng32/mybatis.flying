package indi.mybatis.flying.handlers;

import java.util.UUID;

import indi.mybatis.flying.type.KeyHandler;

public class UuidWithoutLineKeyHandler implements KeyHandler {

	@Override
	public String getKey() {
		return UUID.randomUUID().toString().replaceAll("\\-", "");
	}

}
