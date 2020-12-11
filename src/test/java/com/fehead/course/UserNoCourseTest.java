package com.fehead.course;

import com.fehead.course.compoment.UserGeneratorNoClassTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
