package com.fehead.course;

import com.fehead.course.compoment.UserGeneratorNoClassTask;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.service.NoCourseService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashSet;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-10 17:40
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserNoCourseTest {

    @Autowired
    UserGeneratorNoClassTask userGeneratorNoClassTask;
    @Test
    public void whenInsertUnitCourse(){
//        userGeneratorNoClassTask.noClassActionSustType(1);
    }

}
