package indi.mybatis.flying.handlers;

import java.util.UUID;

import indi.mybatis.flying.type.KeyHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
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
