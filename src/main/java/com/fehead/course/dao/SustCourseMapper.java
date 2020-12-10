package com.fehead.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.SustCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-04 21:43
 * @Version 1.0
 */
@Mapper
public interface SustCourseMapper extends BaseMapper<SustCourse> {
    @Select("select * from user_course_sust where user_id=#{userId}")
    public List<SustCourse> selectByUserId(long userId);
}
