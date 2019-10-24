package com.fehead.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fehead.response.FeheadResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lmwis
 * @description:
 * @date 2019-08-29 15:19
 * @Version 1.0
 */
@TableName("user_info")
@ApiModel(value = "用户类型返回",parent = FeheadResponse.class)
public class User {

    @TableId(type = IdType.AUTO)
    private long id;

    private String nickname;

    @TableField("tel")
    private String tel;

    @TableField("password_id")
    private long passwordId;

    @TableField("school_id")
    private long schoolId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public long getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(long passwordId) {
        this.passwordId = passwordId;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }
}
