package indi.mybatis.flying.type;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
public interface KeyHandler {

	/**
	 * The custom primary key generator needs to implement this interface
	 * 
	 * @return key
	 * 
	 */
	public Object getKey();

}
