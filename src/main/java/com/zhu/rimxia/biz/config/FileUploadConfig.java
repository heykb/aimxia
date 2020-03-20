package com.zhu.rimxia.biz.config;


import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //*设置单文件上传最大*//*
        factory.setMaxFileSize("100MB");
        //*设置上传总数据最大*//*
        //factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}
