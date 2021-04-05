package indi.mybatis.flying.model;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2019年7月8日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    // 通过name获取 Bean.
    public static Object getBean(String name) {
        return context.getBean(name);

    }

    // 通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    // 通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    // 获取环境变量
    public static <T> T getEnvironmentProperty(String key, Class<T> targetClass, T defaultValue) {
        if (key == null || targetClass == null) {
            throw new NullPointerException();
        }

        T value = null;
        if (context != null) {
            value = context.getEnvironment().getProperty(key, targetClass, defaultValue);
        }
        return value;
    }
}
