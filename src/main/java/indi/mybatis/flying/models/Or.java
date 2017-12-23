package indi.mybatis.flying.models;

public class Or<T> {

	public Or(T value, int... appear) {
		this.value = value;
		this.appear = appear;
	}

	private T value;

	private int[] appear;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public int[] getAppear() {
		return appear;
	}

	public void setAppear(int[] appear) {
		this.appear = appear;
	}

}
