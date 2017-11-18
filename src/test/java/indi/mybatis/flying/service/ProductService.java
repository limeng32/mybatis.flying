package indi.mybatis.flying.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.ProductMapper;
import indi.mybatis.flying.pojo.Product;

@Service
public class ProductService implements ProductMapper {

	@Autowired
	private ProductMapper mapper;

	@Override
	public Product select(Object id) {
		return mapper.select(id);
	}

	@Override
	public void insert(Product t) {
		mapper.insert(t);
	}

	@Override
	public void insert2(Product t) {
		mapper.insert2(t);
	}

	@Override
	public void insert3(Product t) {
		mapper.insert3(t);
	}

	@Override
	public void insertMilliSecond(Product t) {
		mapper.insertMilliSecond(t);
	}

	@Override
	public Product selectOne(Product t) {
		return mapper.selectOne(t);
	}

}