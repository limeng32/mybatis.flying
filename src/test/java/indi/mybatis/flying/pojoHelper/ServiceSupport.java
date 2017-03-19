package indi.mybatis.flying.pojoHelper;

import java.util.Collection;

public abstract class ServiceSupport<T extends PojoSupport<T>> implements ServiceFace<T> {

	protected T supportSelect(MapperFace<T> mapper, Object id) {
		return mapper.select(id);
	}

	protected Collection<T> supportSelectAll(MapperFace<T> mapper, T t) {
		return mapper.selectAll(t);
	}

	protected T supportSelectOne(MapperFace<T> mapper, T t) {
		return mapper.selectOne(t);
	}

	protected void supportInsert(MapperFace<T> mapper, T t) {
		mapper.insert(t);
	}

	protected int supportUpdate(MapperFace<T> mapper, T t) {
		return mapper.update(t);
	}

	protected int supportUpdatePersistent(MapperFace<T> mapper, T t) {
		return mapper.updatePersistent(t);
	}

	protected int supportDelete(MapperFace<T> mapper, T t) {
		return mapper.delete(t);
	}

	protected int supportCount(MapperFace<T> mapper, T t) {
		return mapper.count(t);
	}
}
