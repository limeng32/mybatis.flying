package indi.mybatis.flying.exceptions;

public class Configurer2Exception extends Exception {
	private static final long serialVersionUID = 1L;

	public Configurer2Exception(String message) {
		super(message);
	}

	public Configurer2Exception(String message, Throwable throwable) {
		super(message, throwable);
	}

	public Configurer2Exception(Enum<?> e) {
		super(e.name());
	}
}
