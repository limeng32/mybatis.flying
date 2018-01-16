package indi.mybatis.flying.pojo;

public enum LogStatus {

	b("北京"), t("天津"), s("上海");

	private final String text;

	private LogStatus(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
}
