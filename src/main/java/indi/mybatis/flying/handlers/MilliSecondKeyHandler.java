package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
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
