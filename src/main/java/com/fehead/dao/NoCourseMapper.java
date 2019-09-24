package com.fehead.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.dao.entity.NoCourse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-11 20:04
 * @Version 1.0
 */
public interface NoCourseMapper extends BaseMapper<NoCourse> {

    @Select("select distinct period,week from no_class where user_id=#{user_id}")
    List<NoCourse> selectListDistinct(long userId);
}
