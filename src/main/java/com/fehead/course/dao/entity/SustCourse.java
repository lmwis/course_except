package com.fehead.course.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-04 21:39
 * @Version 1.0
 */
@TableName("user_course_sust")
@Data
public class SustCourse extends Course{

    @TableId(type = IdType.AUTO)
    private long id;

    /**
     * 用户学号
     */
    private String username;
    /**
     * 课程姓名
     */
    private String courseName;

    /**
     * 老师姓名
     */
    private String teacherName;

    /**
     * 教室地点
     */
    private String classroom;

}
