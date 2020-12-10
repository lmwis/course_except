package com.fehead.course.service;

import com.fehead.course.dao.entity.Course;

import java.util.Collection;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-10 14:40
 * @Version 1.0
 */
public interface NoCourseService {

    /**
     * 删除用户单元无课
     * @param userId 用户
     */
    public void deleteUnitNoCourseByUserId(long userId);

    /**
     * 插入DB单元无课
     * @param noCourses set
     */
    public void insertUnitNoCourse(Collection<Course> noCourses);
}
