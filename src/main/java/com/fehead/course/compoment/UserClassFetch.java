package com.fehead.course.compoment;

import org.springframework.stereotype.Component;

/**
 * @Description: 发送请求从教务处获取课表
 *  尝试链式调用
 * @Author: lmwis
 * @Date 2020-11-26 11:01
 * @Version 1.0
 */
@Component
public class UserClassFetch {
    private static final String LOGIN_URL = "";

    // 调用链
    // private UserClassFetch userClassFetchChain;

    /**
     * 登陆
     * @param username 用户名
     * @param password 密码
     * @return 调用链
     */
    public UserClassFetch login(String username,String password){
        // 登陆

        // 获取cookie

        return this;
    }

    /**
     * 对获取到的数据进行解析
     * @param content 数据字符串
     */
    public void doResolve(String content){
    }

    public String block(){
        return "";
    }

}
