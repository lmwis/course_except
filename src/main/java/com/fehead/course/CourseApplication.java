package com.fehead.course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@RestController
@MapperScan("com.fehead.course.dao")
@EnableSwagger2
@EnableAsync
public class CourseApplication {

    public static void main( String[] args ) {

        SpringApplication.run(CourseApplication.class,args);
    }
}
