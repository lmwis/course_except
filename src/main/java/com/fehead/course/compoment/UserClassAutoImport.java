package com.fehead.course.compoment;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.reflect.Reflection;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.tools.javadoc.Main.execute;

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

    private static final String LOGIN_CAS_URL="http://login.sust.edu.cn/cas/login?service=http%3A%2F%2Fmy.sust.edu.cn%2Fc%2Fportal%2Flogin";
    private static final String COURSE_INFO_URL="http://bkjw.sust.edu.cn/eams/studentCourseTable!courseTable.action?setting.kind=class&setting.forSemester=1&semester.id=102&ids=3505";
    private static final String CURRENTMENU_KEY="currentMenu";
    private static final String _EVENTID_KEY="_eventId";
    private static final String GEOLOCATION_KEY="geolocation";
    private static final String SUBMIT_KEY="submit";

    private static final String CURRENTMENU="1";
    private static final String _EVENTID="submit";
    private static final String GEOLOCATION="";
    private static final String SUBMIT="%E7%A8%8D%E7%AD%89%E7%89%87%E5%88%BB%E2%80%A6%E2%80%A6";
    private static final String USERNAME="201806020527";
    private static final String PASSWORD="conanyuan1.";
    // 从页面获取
    private static String execution;
    private static final String EXECUTION_KEY = "execution";
    private static final String executionRegex = "(?<=<input type=\"hidden\" name=\"execution\" value=\").*?(?=\")";

    public void prepareCASLogin(){
        // 请求获取
        String result1= HttpUtil.get(LOGIN_CAS_URL);
        // 正则匹配
        Pattern pattern = Pattern.compile(executionRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(result1);
        if (matcher.find()) {
            execution= matcher.group();
        }
        // 参数封装，发送请求
        Map<String,Object> params = new HashMap<>();
        params.put("username",USERNAME);
        params.put("password",PASSWORD);
        params.put(CURRENTMENU_KEY,CURRENTMENU);
        params.put(_EVENTID_KEY,_EVENTID);
        params.put(GEOLOCATION_KEY,GEOLOCATION);
        params.put(SUBMIT_KEY,SUBMIT);
        params.put(EXECUTION_KEY,execution);
        HttpRequest form = HttpRequest.post(LOGIN_CAS_URL).form(params).setFollowRedirects(false);
        HttpResponse execute =form.execute();
        String location = execute.header(Header.LOCATION);
        String tgc = execute.getCookie("TGC").getValue();
        execute.getCookies().forEach(k-> System.out.println(k.getName()+":"+k.getValue()));
//        String qingcloudelb = execute.getCookie("QINGCLOUDELB").getValue();
        logger.info("header:"+location);
        logger.info("tgc:"+tgc);
        // 获取JSESSIONID
        HttpResponse locationResponse = HttpRequest.get(location).execute();
        String jsessionid = locationResponse.getCookie("JSESSIONID").getValue();
        logger.info(jsessionid);
        HttpRequest httpRequest = HttpRequest.get(COURSE_INFO_URL);
//        HttpCookie jsessionidCookie = new HttpCookie("JSESSIONID",jsessionid);
//        HttpCookie gsessinoidCookie = new HttpCookie("GSESSIONID",jsessionid);
//        HttpCookie qingcloudelbCookie = new HttpCookie("QINGCLOUDELB",qingcloudelb);
//        HttpCookie semesterIdCookie = new HttpCookie("semester.id","102");
//        httpRequest.cookie(jsessionidCookie,gsessinoidCookie,qingcloudelbCookie,semesterIdCookie).setFollowRedirects(false);
//        httpRequest.cookie(jsessionidCookie,gsessinoidCookie,semesterIdCookie).setFollowRedirects(false);
        String cookieStr="semester.id=102;JSESSIONID="+jsessionid+";GSESSIONID="+jsessionid;
        httpRequest.header("Cookie",cookieStr);
        logger.info(httpRequest.toString());
        HttpResponse body = httpRequest.execute();
        logger.info(body.toString());
    }

}
