package indi.mybatis.flying.pojoHelper;

import java.util.Collection;

public interface MapperFace<T> {

	public T select(Object id);

	public Collection<T> selectAll(T t);

	public T selectOne(T t);

	public void insert(T t);

	public int update(T t);

	public int updatePersistent(T t);

	public int delete(T t);

	public int count(T t);
}
