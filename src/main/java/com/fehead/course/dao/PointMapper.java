package com.fehead.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.course.dao.entity.SustPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lmwis
 * @description:
 * @date 2019-08-28 20:20
 * @Version 1.0
 */
@Mapper
public interface PointMapper extends BaseMapper<SustPoint> {
}
