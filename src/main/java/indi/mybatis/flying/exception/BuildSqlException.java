package indi.mybatis.flying.exception;

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
