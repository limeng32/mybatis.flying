package indi.mybatis.flying.pojoHelper;

public abstract class PojoSupport<T extends PojoSupport<T>> implements PojoFace<T> {

	@Override
	abstract public Object getId();

	@Override
	public int hashCode() {
		return (getId() == null) ? 0 : getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PojoSupport<?> other = (PojoSupport<?>) obj;
		if (getId() == null) {
			return false;
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

}
