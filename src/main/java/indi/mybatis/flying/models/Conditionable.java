package indi.mybatis.flying.models;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public interface Conditionable {

	public Limitable getLimiter();

	public void setLimiter(Limitable limiter);

	public Sortable getSorter();

	public void setSorter(Sortable sorter);

	public String dot = ".";

	public enum Sequence {
		asc, desc
	}

}
