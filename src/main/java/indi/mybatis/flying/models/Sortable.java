package indi.mybatis.flying.models;

import java.util.List;

import indi.mybatis.flying.pagination.Order;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public interface Sortable {

	public void addOrder();

	public StringBuilder toSql();
	
	public Object getObject();
	
	public List<Order> getOrderList();
}
