package com.fehead.course.compoment;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 课表自动导入
 * 流程：
 * 1.访问 http://login.sust.edu.cn/cas/login?service=http%3A%2F%2Fmy.sust.edu.cn%2Fc%2Fportal%2Flogin
 * ，从页面中解析 currentMenu execution _eventId geolocation submit字段
 * 2.再次post访问 http://login.sust.edu.cn/cas/login?service=http%3A%2F%2Fmy.sust.edu.cn%2Fc%2Fportal%2Flogin
 * 带上用户名密码参数进行表单提交
 * 3.从步骤2返回的header中拿到Location，向Location发送Get请求
 * 4.从步骤3返回header中拿到 JSESSIONID，
 * 5.设置JSESSIONID cookie，向课表页面请求数据
 * 6.解析js代码获得课表
 *
 * (?<=name:").*?(?=")
 * <(input type="hidden" name="execution" value="")[^>]*>.*?
 * (?<=<input type="hidden" name="execution" value=").*?(?=")
 * @Author: lmwis
 * @Date 2020-11-26 19:35
 * @Version 1.0
 */
@Component
@Slf4j
public class UserClassAutoImport {

    private static final Logger logger = LoggerFactory.getLogger(UserClassAutoImport.class);
    private static final String HOST="http://bkjw.sust.edu.cn";
    private static final String LOGIN_CAS_URL="http://login.sust.edu.cn/cas/login?service=http%3A%2F%2Fbkjw.sust.edu.cn%3A80%2Feams%2Fsso%2Flogin.action%3FtargetUrl%3Dbase64aHR0cDovL2Jrancuc3VzdC5lZHUuY246ODAvZWFtcy9ob21lLmFjdGlvbg%3D%3D";
    // 两个请求都可以，执行一个就行
    private static final String PRE_COURSE_INFO_URL_1=HOST+"/eams/studentCourseTable.action?_=1606800721378";
//    private static final String PRE_COURSE_INFO_URL_2=HOST+"/eams/dataQuery.action";
    private static final String SEARCH_COURSE_URL=HOST+"/eams/studentCourseTable!search.action";
    private static final String KEY_1="currentMenu";
    private static final String KEY_2="_eventId";
    private static final String GEOLOCATION_KEY="geolocation";
    private static final String SUBMIT_KEY="submit";

    private static final String VALUE_1="1";
    private static final String VALUE_2="submit";
    private static final String GEOLOCATION="";
    private static final String SUBMIT="%E7%A8%8D%E7%AD%89%E7%89%87%E5%88%BB%E2%80%A6%E2%80%A6";
    private static final String USERNAME="201806020527";
    private static final String PASSWORD="conanyuan1.";
    // 从页面获取
    private static String execution;
    private static final String EXECUTION_KEY = "execution";
    private static final String executionRegex = "(?<=<input type=\"hidden\" name=\"execution\" value=\").*?(?=\")";
    private static final String courseGuideRegex = "(?<=<a href=\").*?(?=\" target=\"_blank\" title=\"查看学生课表\">)";
    private static String userCourseInfoUrl;

    public void prepareCASLogin() throws BusinessException {
        // 1.第一次请求登录页，从页面隐藏中解析出execution值
        String result1= HttpUtil.get(LOGIN_CAS_URL);
        // 正则匹配
        Pattern pattern = Pattern.compile(executionRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(result1);
        if (matcher.find()) {
            execution= matcher.group();
        }
        // 参数封装，发送请求
        Map<String, Object> params = packageLoginParams();
        // 2.第二次请求登录页，执行登录，获取Location请求头和TGC
        HttpResponse execute = HttpRequest.post(LOGIN_CAS_URL).form(params).setFollowRedirects(false).execute();
        String location = execute.header(Header.LOCATION);
        // TGC会自动设置
//        String tgc = execute.getCookie("TGC").getValue();
//        logger.info(tgc);
        // 3.Get访问Location,获取JSESSIONID，JSESSIONID会自动加入cookie中
        HttpRequest.get(location).execute();
//        String jsessionid = locationResponse.getCookie("JSESSIONID").getValue();
        // 4.登录成功后再获取课表页面之前需要预先进行一次请求，原因不详。以下两种请求均可
        HttpRequest.get(PRE_COURSE_INFO_URL_1).header("Referer","http://bkjw.sust.edu.cn/eams/studentCourseTable!search.action?semester.id=102&courseTableType=class&enabled=1").execute();
//        HttpRequest.post(PRE_COURSE_INFO_URL_2).header("Referer","http://bkjw.sust.edu.cn/eams/studentCourseTable!search.action?semester.id=102&courseTableType=class&enabled=1").execute();
        // 5.获取用户课表url
        Map<String, Object> userCourseQueryParams = packageUserCourseQueryParams();
        String userCourseGuide = HttpRequest.post(SEARCH_COURSE_URL).form(userCourseQueryParams).execute().body();
        // 解析出url
        Pattern courseGuidePattern = Pattern.compile(courseGuideRegex, Pattern.DOTALL);
        Matcher courseGuideMatcher = courseGuidePattern.matcher(userCourseGuide);
        String userCourseUrlTemp="";
        if (courseGuideMatcher.find()) {
            userCourseUrlTemp= courseGuideMatcher.group();
        }else {
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL);
        }
        // 去处中间多余字符串
        StringBuilder userCourseUrlBuffer = new StringBuilder();
        for (String s : userCourseUrlTemp.split("%21search")) {
            userCourseUrlBuffer.append(s);
        }
        // 拼装最终当前用户课表地址
        userCourseInfoUrl = HOST+userCourseUrlBuffer.toString();
        logger.info(userCourseInfoUrl);
        // 6.获取用户课表页面
        HttpResponse courseHtml = HttpRequest.get(userCourseInfoUrl).execute();
        logger.info(courseHtml.toString());
    }

    /**
     * 封装查询用户课表url的post请求参数
     * @return
     */
    private Map<String, Object> packageUserCourseQueryParams() {
        Map<String,Object> params = new HashMap<>();
        params.put("std.project.id","1");
        params.put("std.code",USERNAME);
        params.put("std.name","");
        params.put("std.grade","");
        params.put("std.department.id","");
        params.put("std.major.id","");
        params.put("adminclassName","");
        params.put("std.active","1");
        params.put("semester.id","102");
        params.put("courseTableType","std");
        return params;
    }

    /**
     * 封装login的post请求参数
     * @return
     */
    private Map<String, Object> packageLoginParams() {
        Map<String,Object> params = new HashMap<>();
        params.put("username",USERNAME);
        params.put("password",PASSWORD);
        params.put(KEY_1,VALUE_1);
        params.put(KEY_2,VALUE_2);
        params.put(GEOLOCATION_KEY,GEOLOCATION);
        params.put(SUBMIT_KEY,SUBMIT);
        params.put(EXECUTION_KEY,execution);
        return params;
    }

}
