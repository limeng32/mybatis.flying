package indi.mybatis.flying.exception;

public class AutoMapperException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AutoMapperException(String message) {
		super(message);
	}

	public AutoMapperException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AutoMapperException(Enum<?> e) {
		super(e.toString());
	}
}
