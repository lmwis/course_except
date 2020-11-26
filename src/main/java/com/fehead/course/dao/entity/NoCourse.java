package com.fehead.course.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author lmwis
 * @description: 数据库存储无课表的单元格式，所有的无课表单元，一条数据表示某一周的一节课
 * @date 2019-09-11 20:04
 * @Version 1.0
 */

@TableName("no_class")
public class NoCourse {

    @TableId(type = IdType.AUTO)
    private long id;

    private long userId;

    private String week;

    private int period;

    private String weeks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }
}
