package indi.mybatis.flying.models;

public class Or {

	public Or(Object... value) {
		this.value = value;
	}

	private Object[] value;

	public Object[] getValue() {
		return value;
	}

	public void setValue(Object[] value) {
		this.value = value;
	}

}
