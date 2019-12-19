package indi.mybatis.flying.pagination;

import java.util.LinkedList;
import java.util.List;

import indi.mybatis.flying.models.Sortable;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class SortParam implements Sortable {

	public SortParam(Order... orders) {
		list = new LinkedList<>();
		for (Order order : orders) {
			list.add(order);
		}
	}

	List<Order> list;

	@Override
	public void addOrder() {
		// make sonar happy
	}

	@Override
	public String toSql() {
		StringBuilder ret = new StringBuilder();
		if (list != null) {
			ret.append(" order by");
			for (Order order : list) {
				ret.append(order.toSql());
			}
			if (ret.lastIndexOf(",") == ret.length() - 1) {
				ret.deleteCharAt(ret.length() - 1);
			}
		}
		return ret.toString();
	}

}
