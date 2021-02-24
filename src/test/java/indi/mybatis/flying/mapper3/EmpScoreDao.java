package indi.mybatis.flying.mapper3;

import java.util.Collection;

import org.apache.ibatis.annotations.Mapper;

import indi.mybatis.flying.pojo.EmpScore;

/**
 * @author 刘文杰
 * @ClassName EmpScoreDao
 * @Description
 * @date 2021/1/26 16:47
 * @email wj963846244@163.com
 * @since JDK 1.8
 */
@Mapper
public interface EmpScoreDao {
	public int insert(EmpScore score);

	public EmpScore select(Long id);

	public Collection<EmpScore> selectAll(EmpScore score);

	public EmpScore selectOne(EmpScore score);

	public int update(EmpScore score);

	public int updatePersistent(EmpScore score);

	public int delete(EmpScore score);

	public int count(EmpScore score);

	public int insertBatch(Collection<EmpScore> collection);
	
	public int insertAes(EmpScore score);
	
	public EmpScore selectAes(EmpScore score);
}
