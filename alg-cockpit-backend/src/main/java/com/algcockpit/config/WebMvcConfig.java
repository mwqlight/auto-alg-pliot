package com.algcockpit.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC配置类
 * 
 * @author Algorithm Cockpit Team
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    /**
     * 配置消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        
        // 配置FastJson
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // 配置序列化规则
        // config.setSerializerFeatures(
        //     SerializerFeature.WriteMapNullValue,
        //     SerializerFeature.WriteNullListAsEmpty,
        //     SerializerFeature.WriteNullStringAsEmpty,
        //     SerializerFeature.WriteNullNumberAsZero,
        //     SerializerFeature.WriteNullBooleanAsFalse,
        //     SerializerFeature.PrettyFormat
        // );
        
        converter.setFastJsonConfig(config);
        
        // 支持的类型
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(mediaTypes);
        
        converters.add(0, converter);
    }
}