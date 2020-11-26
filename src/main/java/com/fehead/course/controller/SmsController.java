package com.fehead.course.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.course.properties.SecurityProperties;
import com.fehead.course.service.RedisService;
import com.fehead.course.service.SmsService;
import com.fehead.course.service.model.ValidateCode;
import com.fehead.course.utils.CheckEmailAndTelphoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 15:32
 * @Version 1.0
 */
@RestController
@RequestMapping("/sys/sms")
public class SmsController extends BaseController {
    enum SmsAction{
        REGISTER("register"),
        LOGIN("login"),
        RESET("reset")
        ;
        private String actionStr;
        SmsAction(String actionStr) {
            this.actionStr = actionStr;
        }
        public String value() {
            return actionStr;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private SmsService smsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 提供手机号和当前行为，根据行为发送相应短信
     * @param request
     * @param response
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = "/send")
    public FeheadResponse sendSms(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        String telphone = request.getParameter("tel");
        logger.info("手机号：" + telphone);
        String action = request.getParameter("action");

        // 检查手机号是否合法
        if (!CheckEmailAndTelphoneUtil.checkTelphone(telphone)) {
            logger.info("手机号不合法");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号不合法");
        }


        // 检查验证码在60秒内是否已经发送
        if (action.equals(SmsAction.REGISTER.actionStr)) {
            if (smsService.check(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphone)) {
                ValidateCode code = (ValidateCode) redisService.get(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphone);
                if (!code.isExpired(60)) {
                    logger.info("验证码已发送");
                    throw new BusinessException(EmBusinessError.SMS_ALREADY_SEND);
                } else {
                    redisService.remove(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphone);
                }
            }
        } else if (action.equals(SmsAction.LOGIN.actionStr)) {
            if (smsService.check(securityProperties.getSmsProperties().getLoginPreKeyInRedis() + telphone)) {
                ValidateCode code = (ValidateCode) redisService.get(securityProperties.getSmsProperties().getLoginPreKeyInRedis() + telphone);
                if (!code.isExpired(60)) {
                    logger.info("验证码已发送");
                    throw new BusinessException(EmBusinessError.SMS_ALREADY_SEND);
                } else {
                    redisService.remove(securityProperties.getSmsProperties().getLoginPreKeyInRedis() + telphone);
                }
            }
        } else if (action.equals(SmsAction.RESET.actionStr)) {
            if (smsService.check(securityProperties.getSmsProperties().getLoginPreKeyInRedis() + telphone)) {
                ValidateCode code = (ValidateCode) redisService.get(securityProperties.getSmsProperties().getLoginPreKeyInRedis() + telphone);
                if (!code.isExpired(60)) {
                    logger.info("验证码已发送");
                    throw new BusinessException(EmBusinessError.SMS_ALREADY_SEND);
                } else {
                    redisService.remove(securityProperties.getSmsProperties().getResetPreKeyInRedis() + telphone);
                }
            }
        }else {
            logger.info("action异常");
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL, "action异常");
        }

        // 根据行为选择模板发送短信  0为注册模板，1为登录模板，2为重置模版
        if (action.equals(SmsAction.LOGIN.actionStr)) {
            smsService.send(telphone, 1);
        } else if (action.equals(SmsAction.REGISTER.actionStr)) {
            smsService.send(telphone, 0);
        } else if(action.equals(SmsAction.RESET.actionStr)){
            smsService.send(telphone, 2);
        }else {
            logger.info("action异常");
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "action异常");
        }

        return CommonReturnType.create(telphone);
    }

    /**
     * 对手机号和验证码进行校验
     * @param request
     * @param response
     * @return
     * @throws BusinessException
     */
    @PutMapping(value = "/validate")
    public FeheadResponse validateSms(HttpServletRequest request, HttpServletResponse response) throws BusinessException {

        String telphoneInRequest = request.getParameter("tel");
        String codeInRequest = request.getParameter("code");
        String smsKey = "";
        logger.info("手机号：" + telphoneInRequest);
        logger.info("验证码：" + codeInRequest);
        if (!CheckEmailAndTelphoneUtil.checkTelphone(telphoneInRequest)) {
            logger.info("手机号不合法");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号不合法");
        }
        if (codeInRequest.isEmpty()) {
            logger.info("验证码为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "验证码为空");
        }
        if (registerValidate(telphoneInRequest, codeInRequest)) {
            smsKey = passwordEncoder.encode(telphoneInRequest);
            logger.info("密钥：" + smsKey);
            redisService.set("sms_key_"+ telphoneInRequest, smsKey, new Long(30*60));
        }

        return CommonReturnType.create(smsKey);
    }
    private boolean registerValidate(String telphoneInRequest, String codeInRequest) throws BusinessException {
        ValidateCode smsCode = new ValidateCode();

        // 检查redis中是否存有该手机号验证码
        if (!redisService.exists(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphoneInRequest)) {
            if (!redisService.exists(securityProperties.getSmsProperties().getResetPreKeyInRedis() + telphoneInRequest)) {
                logger.info("验证码不存在");
                throw new BusinessException(EmBusinessError.SMS_ISNULL);
            }else{
                smsCode = (ValidateCode)redisService.get(securityProperties.getSmsProperties().getResetPreKeyInRedis() + telphoneInRequest);
            }
        }else {
            smsCode = (ValidateCode)redisService.get(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphoneInRequest);
        }


        if (StringUtils.isBlank(codeInRequest)) {
            logger.info("验证码不能为空");
            throw new BusinessException(EmBusinessError.SMS_BLANK);
        }

        if (smsCode == null) {
            logger.info("验证码不存在");
            throw new BusinessException(EmBusinessError.SMS_ISNULL);
        }


        if (!passwordEncoder.matches(codeInRequest, smsCode.getCode())) {
            logger.info("验证码不匹配");
            throw new BusinessException(EmBusinessError.SMS_ILLEGAL);
        }

        redisService.remove(securityProperties.getSmsProperties().getRegisterPreKeyInRedis() + telphoneInRequest);
        redisService.remove(securityProperties.getSmsProperties().getResetPreKeyInRedis() + telphoneInRequest);


        return true;
    }


}
