package indi.mybatis.flying.type;

public interface KeyHandler {

	/** The custom primary key generator needs to implement this interface */
	public Object getKey();

}
