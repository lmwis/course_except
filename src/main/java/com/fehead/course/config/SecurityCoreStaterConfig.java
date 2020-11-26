package com.fehead.course.config;

import com.fehead.course.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 16:04
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreStaterConfig {
}
