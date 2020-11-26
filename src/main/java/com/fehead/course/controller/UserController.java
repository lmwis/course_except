package com.fehead.course.controller;

import com.fehead.course.compoment.UserGeneratorNoClassTask;
import com.fehead.course.dao.PasswordMapper;
import com.fehead.course.dao.UserMapper;
import com.fehead.course.dao.entity.Password;
import com.fehead.course.dao.entity.User;
import com.fehead.course.service.RedisService;
import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CheckEmailAndTelphoneUtil;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 11:20
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;
    @Autowired
    private RedisService redisService;

    @Autowired
    PasswordMapper passwordMapper;

    @Autowired
    UserGeneratorNoClassTask userGeneratorNoClassTask;


    @PostMapping()
    @ApiOperation(value = "用户注册", response = FeheadResponse.class)
    public FeheadResponse register(String nickname, String password, String tel, String sms_key) throws BusinessException {


        User user = new User();
        if (StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(tel)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if (!CheckEmailAndTelphoneUtil.checkTelphone(tel)) {
            logger.info("手机号不合法");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号不合法");
        }
        if (password.isEmpty()) {
            logger.info("密码为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "密码为空");
        }
        if (nickname.isEmpty()) {
            logger.info("昵称为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "昵称为空");
        }
        if (redisService.exists("sms_key_" + tel)) {
            if (redisService.get("sms_key_" + tel).equals(sms_key)) {
                redisService.remove("sms_key_" + tel);
                // 进行注册
                // 注册流程
                User user1 = userMapper.selectByUsername(nickname);
                if (user1 != null) { // 昵称不能重复
                    throw new BusinessException(EmBusinessError.USER_ALREAY_EXIST);
                }
                user1 = userMapper.selectByUserTel(tel);
                if (user1 != null) { // 手机号不能重复
                    throw new BusinessException(EmBusinessError.USER_ALREAY_EXIST);
                }

                user.setTel(tel);
                user.setNickname(nickname);
                Password inPassword = new Password();
                inPassword.setPassword(passwordEncoder.encode(password));
                passwordMapper.insert(inPassword);
                user.setPasswordId(inPassword.getId());
                userMapper.insert(user);

            } else {
                logger.info("操作不合法");
                throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL);
            }
        } else {
            logger.info("操作不合法");
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL);
        }

        // 注册成功初始化用户无课表
        // 异步执行
        userGeneratorNoClassTask.noClassAction(user.getId());


        return CommonReturnType.create("注册成功");

    }

    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public FeheadResponse resetPassword(String tel, String password, String sms_key) throws BusinessException {

        if (password.isEmpty()) {
            logger.info("密码为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "密码为空");
        }

        if (redisService.exists("sms_key_" + tel)) {
            if (redisService.get("sms_key_" + tel).equals(sms_key)) {
                redisService.remove("sms_key_" + tel);
                // 密码重置
                User user = userMapper.selectByUserTel(tel);
                Password passwordEntity = passwordMapper.selectById(user.getPasswordId());
                passwordEntity.setPassword(passwordEncoder.encode(password));
                passwordMapper.insert(passwordEntity);

            } else {
                logger.info("操作不合法");
                throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL);
            }
        } else {
            logger.info("操作不合法");
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL);
        }
        return CommonReturnType.create("重置成功");

    }

}
