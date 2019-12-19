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
public class UuidWithoutLineKeyHandler implements KeyHandler {

	private UuidWithoutLineKeyHandler() {
	}

	private static class InnerInstance {

		private InnerInstance() {

		}

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
