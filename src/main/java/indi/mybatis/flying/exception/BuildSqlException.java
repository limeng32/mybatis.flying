package indi.mybatis.flying.exception;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class BuildSqlException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BuildSqlException(String message) {
		super(message);
	}

	public BuildSqlException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BuildSqlException(Enum<?> e) {
		super(e.toString());
	}
}
