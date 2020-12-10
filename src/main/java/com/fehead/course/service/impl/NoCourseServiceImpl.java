package com.fehead.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fehead.course.dao.NoCourseMapper;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.NoCourse;
import com.fehead.course.service.NoCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 无课表相关数据库操作
 * @Author: lmwis
 * @Date 2020-12-10 14:40
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class NoCourseServiceImpl implements NoCourseService {

    private final NoCourseMapper noCourseMapper;

    /**
     * 删除用户无课表单元
     * @param userId userid未校验
     */
    @Override
    public void deleteUnitNoCourseByUserId(long userId) {
        QueryWrapper<NoCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        noCourseMapper.delete(queryWrapper);
    }

    /**
     *
     * @param noCourses
     */
    public void insertUnitNoCourse(Collection<Course> noCourses){
        // 写入数据库
        for (Course c : noCourses) {
            NoCourse noCourse = new NoCourse();
            noCourse.setUserId(c.getUserId());
            noCourse.setWeeks(c.getWeeks());
            noCourse.setPeriod(c.getPeriod());
            noCourse.setWeek(c.getWeek());
            noCourseMapper.insert(noCourse);
        }
    }
}
