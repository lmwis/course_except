package com.fehead;

import com.fehead.service.CourseService;
import com.fehead.dao.GroupMapper;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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

    @Test
    public void whenJudgeUserInGroup(){

        int i = groupMapper.selectUserIdAndGroupIdEqu(3, 1);
        System.out.println(i);
    }

    @Test
    public void selectByUserIdAndWeeks(){

        courseService.selectByUserIdAndWeeks(1,2).forEach(k->{
            System.out.println(new ReflectionToStringBuilder(k));
        });
    }

    @Test
    public void judgeWeeksTempAnd(){

        boolean b = courseService.judgeWeeksTempAnd("11000", 2);
        System.out.println(b);
    }

}
