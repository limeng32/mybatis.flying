package indi.mybatis.flying.exception;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
public class SnowFlakeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SnowFlakeException(String message) {
		super(message);
	}

	public SnowFlakeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SnowFlakeException(Enum<?> e) {
		super(e.toString());
	}
}
