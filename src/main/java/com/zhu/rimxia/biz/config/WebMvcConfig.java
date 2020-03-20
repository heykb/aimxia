package com.zhu.rimxia.biz.config;

import com.zhu.rimxia.biz.filter.CorsFilter;
import com.zhu.rimxia.biz.jwt.RedisTokenStore;
import com.zhu.rimxia.biz.jwt.TokenInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

@ServletComponentScan(basePackageClasses = {CorsFilter.class})
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RedisTokenStore redisTokenStore;


    public void addResourceHandlers(ResourceHandlerRegistry registry){
        /**
         * 设置静态资源
         */
        registry.addResourceHandler("/video/**")
                .addResourceLocations("file:D:/FTP/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(redisTokenStore))
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }
}
