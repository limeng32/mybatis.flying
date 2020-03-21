package indi.mybatis.flying.config;

import java.math.BigInteger;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 
 * @date 2019年7月8日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@ComponentScan
@Configuration
@EnableAutoConfiguration
public class FastjsonAdapter extends WebMvcConfigurerAdapter {
	public static void main(String[] args) {
		SpringApplication.run(FastjsonAdapter.class, args);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteEnumUsingToString);
		SerializeConfig serializeConfig = SerializeConfig.globalInstance;
		serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
		serializeConfig.put(Long.class, ToStringSerializer.instance);
		serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
		fastJsonConfig.setSerializeConfig(serializeConfig);

		fastConverter.setFastJsonConfig(fastJsonConfig);

		converters.add(fastConverter);
	}

}
