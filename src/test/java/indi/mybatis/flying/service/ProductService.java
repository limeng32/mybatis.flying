package indi.mybatis.flying.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.ProductMapper;
import indi.mybatis.flying.pojo.Product;

@Service
public class ProductService implements ProductMapper {

	@Autowired
	private ProductMapper mapper;

	public Product select(Object id) {
		return mapper.select(id);
	}

	public void insert(Product t) {
		mapper.insert(t);
	}

	@Override
	public Product selectOne(Product t) {
		return mapper.selectOne(t);
	}

}