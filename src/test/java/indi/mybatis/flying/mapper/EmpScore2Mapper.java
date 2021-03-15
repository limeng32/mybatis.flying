package indi.mybatis.flying.mapper;

import java.util.List;

import indi.mybatis.flying.pojo.EmpScore2;

public interface EmpScore2Mapper {

	public EmpScore2 select(Long id);

	public List<EmpScore2> selectAll(EmpScore2 o);

}
