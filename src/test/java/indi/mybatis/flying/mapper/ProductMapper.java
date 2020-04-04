package indi.mybatis.flying.mapper;

import indi.mybatis.flying.pojo.Product;

public interface ProductMapper {

	public Product select(Object id);

	public Product selectOne(Product t);

	public void insert(Product t);

	public void insert2(Product t);

	public void insert3(Product t);

	public void insertMilliSecond(Product t);

	public void insertMilliSecond2(Product t);

	public void insertSnowFlake(Product t);

	public void insertMySnowFlake(Product t);

	public void insertMySnowFlake2(Product t);

	public void insertAsd(Product t);

	public void insertDistributedSnowflake(Product t);
}
