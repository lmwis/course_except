package com.fehead.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.course.dao.entity.Password;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lmwis
 * @description:
 * @date 2019-08-29 16:12
 * @Version 1.0
 */
@Mapper
public interface PasswordMapper extends BaseMapper<Password> {
}
