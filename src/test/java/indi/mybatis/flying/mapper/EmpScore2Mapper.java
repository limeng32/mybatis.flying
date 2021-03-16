package indi.mybatis.flying.mapper;

import java.util.Collection;
import java.util.List;

import indi.mybatis.flying.pojo.EmpScore2;

public interface EmpScore2Mapper {

	public EmpScore2 select(Long id);

	public List<EmpScore2> selectAll(EmpScore2 o);

	public EmpScore2 selectOne(EmpScore2 o);

	public int count(EmpScore2 o);

	public int update(EmpScore2 o);

	public int updatePersistent(EmpScore2 o);

	public int insert(EmpScore2 o);

	public int insertBatch(Collection<EmpScore2> o);

	public int updateBatch(Collection<EmpScore2> o);
}
