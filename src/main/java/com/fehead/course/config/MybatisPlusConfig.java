package com.fehead.course.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: mybatis-plus 配置
 * @Author: lmwis
 * @Date 2020-12-02 13:08
 * @Version 1.0
 */
@MapperScan("com.fehaed.course.dao*")
@Configuration
public class MybatisPlusConfig {
}
