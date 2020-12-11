package com.fehead.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableSwagger2
@EnableAsync
//@Configuration
//@EnableEurekaClient
public class CourseApplication {

    public static void main( String[] args ) {

        SpringApplication.run(CourseApplication.class,args);
    }
}
