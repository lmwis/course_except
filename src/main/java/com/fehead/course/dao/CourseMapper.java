package com.fehead.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.course.dao.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 12:28
 * @Version 1.0
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {


    @Select("select * from user_course where user_id=#{userId}")
    public List<Course> selectByUserId(long userId);

    @Select("select * from user_course where user_id=#{userId} and week=#{week}")
    public List<Course> selectByUserIdAndWeek(int userId,String week);
}
