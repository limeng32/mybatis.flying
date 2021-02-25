package indi.mybatis.flying.mapper3;

import java.util.List;

import indi.mybatis.flying.pojo.Account3;

public interface Account3Dao {

	public int insert(Account3 account3);

	public Account3 select(Integer id);

	public List<Account3> selectAll(Account3 account3);

	public Account3 selectOne(Account3 account3);

	public int count(Account3 account3);
}
