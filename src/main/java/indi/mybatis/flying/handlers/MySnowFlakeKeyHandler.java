package indi.mybatis.flying.handlers;

import indi.mybatis.flying.type.KeyHandler;
import indi.mybatis.flying.utils.SnowflakeIdWorker;

public class MySnowFlakeKeyHandler implements KeyHandler {

	@Override
	public String getKey() {
		return new Long(SnowflakeIdWorker.getInstance().nextId()).toString();
	}

}
