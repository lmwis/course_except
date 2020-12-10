package com.fehead.course.compoment;

import com.fehead.course.dao.CourseMapper;
import com.fehead.course.dao.SustCourseMapper;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.NoCoursePack;
import com.fehead.course.dao.entity.SustCourse;
import com.fehead.course.service.CourseService;
import com.fehead.course.service.NoCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lmwis
 * @description: 异步执行类
 *  删除用户之前打包的无课表
 *  生成用户无课表并打包写入数据库
 * @date 2019-09-22 18:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserGeneratorNoClassTask{

    final CourseService courseService;
    final NoCourseService noCourseService;

    final NoClassGenerator noClassGenerator;

    final CourseMapper courseMapper;

    final SustCourseMapper sustCourseMapper;

//    final NoCourseMapper noCourseMapper;

    /**
     * 异步生成无课表，步骤：(3-4是不是白白操作一次数据库？)
     *  1.数据库中删除用户所有单元无课表(DB)
     *  2.根据用户课表List<Course>类型生成单元无课表
     *  3.写入单元无课表数据表(DB)
     *  4.从该用户单元无课表数据表中取出，封装为无课表(DB)
     *  5.删除已经存在无课表(DB)
     *  6.写入用户无课表数据表(DB)
     * @param userId userid
     */
    @Async   // 支持异步
    public void noClassAction(long userId) {

        // 1.数据库中删除用户所有单元无课表(DB)
        noCourseService.deleteUnitNoCourseByUserId(userId);
        // 2.生成用户的单元无课表
        Collection<Course> noCourses = noClassGenerator.generateNoClass(userId,courseService.selectByUserId((int) userId));
        // 3.写入单元无课表数据库
        noCourseService.insertUnitNoCourse(noCourses);
        // 4.封装无课表
        Collection<NoCoursePack> userNoClassPack = courseService.getUserNoClassPack(userId);
        // 5.删除已经存在打包好的无课表
        courseService.deleteUserNoClassPack(userId);
        // 6.写入数据库
        courseService.savePackNoClass(userNoClassPack);
    }
//    @Async   // 支持异步
    public void noClassActionSustType(long userId) {

        // 1.数据库中删除用户所有单元无课表(DB)
        noCourseService.deleteUnitNoCourseByUserId(userId);
        // 2.生成用户的单元无课表
        Collection<Course> noCourses = noClassGenerator
                .generateNoClass(userId
                        ,new ArrayList<>(sustCourseMapper.selectByUserId((int) userId)));
        // 3.写入单元无课表数据库
        noCourseService.insertUnitNoCourse(noCourses);
        // 4.封装无课表
        Collection<NoCoursePack> userNoClassPack = courseService.getUserNoClassPack(userId);
        // 5.删除已经存在打包好的无课表
        courseService.deleteUserNoClassPack(userId);
        // 6.写入数据库
        courseService.savePackNoClass(userNoClassPack);
    }
}
