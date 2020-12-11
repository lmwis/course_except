package com.fehead.course.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lmwis
 * @description: 数据库存储无课表的单元格式，所有的无课表单元，一条数据表示某一周的一节课
 * @date 2019-09-11 20:04
 * @Version 1.0
 */

@TableName("no_class")
@Data
public class NoCourse {

    @TableId(type = IdType.AUTO)
    private long id;

    private long userId;

    private String week;

    private int period;

    private String weeks;

}
