package com.fehead.course.compoment;

import com.fehead.course.dao.entity.NoCoursePack;
import com.fehead.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author lmwis
 * @description: 异步执行类
 *  删除用户之前打包的无课表
 *  生成用户无课表并打包写入数据库
 * @date 2019-09-22 18:15
 * @Version 1.0
 */
@Component
public class UserGeneratorNoClassTask{

    @Autowired
    CourseService courseService;

    @Autowired
    NoClassGenerator noClassGenerator;

    @Async   // 支持异步
    public void noClassAction(long userId) {

        // 生成用户的单元无课表 并写入数据库
        noClassGenerator.generateNoClass(userId);
        // 封装无课表
        Collection<NoCoursePack> userNoClassPack = courseService.getUserNoClassPack(userId);
        // 删除已经存在打包好的无课表
        courseService.deleteUserNoClassPack(userId);
        // 写入数据库
        courseService.savePackNoClass(userNoClassPack);
    }
}
