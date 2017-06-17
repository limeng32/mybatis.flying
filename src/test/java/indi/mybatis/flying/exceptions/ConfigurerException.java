package indi.mybatis.flying.exceptions;

public class ConfigurerException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConfigurerException(String message) {
		super(message);
	}

	public ConfigurerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ConfigurerException(Enum<?> e) {
		super(e.name());
	}
}
