package com.fehead.course;

import com.fehead.course.service.CourseService;
import com.fehead.course.dao.GroupMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 16:47
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupMapperTest {
    @Autowired
    GroupMapper groupMapper;

    @Autowired
    CourseService courseService;

    @Test
    public void whenInsert(){
        groupMapper.applyGroup(1,1);
    }



}
