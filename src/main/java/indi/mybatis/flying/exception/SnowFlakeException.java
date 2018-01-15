package indi.mybatis.flying.exception;

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
