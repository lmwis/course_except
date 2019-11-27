package com.fehead.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 12:17
 * @Version 1.0
 */
@TableName("user_course")
@Data
public class Course extends NoCourse{

    @TableId(type = IdType.AUTO)
    private long id;

    private String weeksText;

}
