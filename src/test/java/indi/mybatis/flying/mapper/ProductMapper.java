package indi.mybatis.flying.mapper;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Product;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = {}, triggerClass = { Product.class })
public interface ProductMapper {

	@CacheAnnotation(role = CacheRoleType.Observer)
	public Product select(Object id);

	@CacheAnnotation(role = CacheRoleType.Observer)
	public Product selectOne(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insert(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insert2(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insert3(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertMilliSecond(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertMilliSecond2(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertSnowFlake(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertMySnowFlake(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertMySnowFlake2(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertAsd(Product t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public void insertDistributedSnowflake(Product t);
}
