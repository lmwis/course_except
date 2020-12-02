package com.fehead.course.service;

import com.fehead.course.compoment.model.SustCourse;
import com.fehead.course.controller.vo.NoCourse4MutUsers;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.NoCourse4Group;
import com.fehead.course.dao.entity.NoCoursePack;
import com.fehead.lang.error.BusinessException;

import java.util.Collection;
import java.util.List;

/**
 * @author lmwis
 * @description: Course服务接口类
 * @date 2019-09-07 11:44
 * @Version 1.0
 */
public interface CourseService {

    public List<Course> selectByUserId(int userId);

    public List<Course> selectByUserIdAndWeeks(int userId, int weeks);

    public boolean judgeWeeksTempAnd(String weeks,int weeksNum);

    public Collection<NoCoursePack> getUserNoClassPack(long userId);

    public Collection<NoCourse4Group> getGroupNoClassPackOrderByWeeks(int groupId, int weeks);

    public Collection<NoCourse4Group> getGroupNoClassPackOrderByWeeksInclude(int groupId,int weeks);

    public Collection<NoCourse4MutUsers> packNoClass4Group(Collection<NoCourse4Group> classes);

    public void savePackNoClass(Collection<NoCoursePack> resNoCourse);

    void deleteUserNoClassPack(long userId);

    List<Course> getUserCourseFromSust(String username, String password) throws BusinessException;
}
