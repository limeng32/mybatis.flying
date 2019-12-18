package indi.mybatis.flying.cache;

import java.util.Comparator;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
public class ClassComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		return o1.getSimpleName().compareTo(o2.getSimpleName());
	}

}
