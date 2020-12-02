package com.fehead.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.course.dao.entity.Test;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lmwis
 * @description:
 * @date 2019-08-27 10:35
 * @Version 1.0
 */
@Mapper
public interface TestMapper extends BaseMapper<Test> {
}
