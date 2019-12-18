package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class MySnowFlakeKeyHandler implements KeyHandler {

	@Override
	public String getKey() {
		return SnowFlakeKeyHandler.getInstance().getKey();
	}

}
