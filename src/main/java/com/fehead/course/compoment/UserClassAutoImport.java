package com.fehead.course.compoment;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fehead.course.compoment.model.SustCourse;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 课表自动导入
 * 流程：
 * 1.第一次请求登录页，从页面隐藏中解析出execution值
 * 2.第二次请求登录页，执行登录，获取Location请求头和TGC
 * 3.Get访问Location,获取JSESSIONID，JSESSIONID会自动加入cookie中
 * 4.登录成功后再获取课表页面之前需要预先进行一次请求，原因不详。以下两种请求均可
 * 5.获取用户课表url
 * 6.获取用户课表页面
 * 7.解析出用户的每节课程信息
 * 8.转化为系统中课程数据
 * 9.生成无课表
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
    private String username;
    private String password;
    // 从页面获取
    private String execution;
    private static final String EXECUTION_KEY = "execution";
    private static final String executionRegex = "(?<=<input type=\"hidden\" name=\"execution\" value=\").*?(?=\")";
    private static final String courseGuideRegex = "(?<=<a href=\").*?(?=\" target=\"_blank\" title=\"查看学生课表\">)";
    private String userCourseInfoUrl;
    // 课程html
    private String courseHtml;
    CourseResolve courseResolve;

    /**
     * 登录系统拿到用户课表html
     * @param username 学号
     * @param password 密码
     * @throws BusinessException 业务异常
     */
    public UserClassAutoImport prepareCASLogin(String username,String password) throws BusinessException {
        this.username = username;
        this.password = password;

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
        // 登录成功状态码为302，失败为401
        HttpResponse execute = HttpRequest.post(LOGIN_CAS_URL).form(params).setFollowRedirects(false).execute();
        if(execute.getStatus()== HttpStatus.UNAUTHORIZED.value()){
            // 登录失败
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }else if(execute.getStatus()!= HttpStatus.FOUND.value()){
            // 不为302未知错误
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        logger.info("sust教务系统登录成功，用户:"+username);
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
        String userCourseUrlTemp;
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
//        logger.info(userCourseInfoUrl);
        // 6.获取用户课表页面
        courseHtml = HttpRequest.get(userCourseInfoUrl).execute().toString();

        this.courseResolve = new CourseResolve(courseHtml);
//        logger.info(courseHtml.);
        return this;
    }

    /**
     * 封装查询用户课表url的post请求参数
     * @return params
     */
    private Map<String, Object> packageUserCourseQueryParams() {
        Map<String,Object> params = new HashMap<>();
        params.put("std.project.id","1");
        params.put("std.code",username);
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
     * @return params
     */
    private Map<String, Object> packageLoginParams() {
        Map<String,Object> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params.put(KEY_1,VALUE_1);
        params.put(KEY_2,VALUE_2);
        params.put(GEOLOCATION_KEY,GEOLOCATION);
        params.put(SUBMIT_KEY,SUBMIT);
        params.put(EXECUTION_KEY,execution);
        return params;
    }

    public List<SustCourse> doResolve(){
        return courseResolve.doResolve();
    }

    private class CourseResolve{

        final String courseHtml;

        CourseResolve(String courseHtml){
            this.courseHtml = courseHtml;
//            logger.info(courseHtml);
        }

        private final String teacherNameRegx = "(?<=teachers.{0,100}?.name:\").{0,100}?(?=\")";
        private final String courseInfoRegx="(?<=TaskActivity.{0,100}\").{0,1000}?(?=;)";
        private final String jsCourseRegx = "(?<=activity=null;)[\\s\\S]*(?=activity;)";
        private final String oneCourseRegx = "var teachers[\\s\\S]*?table0";
//        private final String courseTimeRegx = "index.{0,100}?([0-9]).{0,100}?([0-9]);";
        private final String dayTimeRegx ="(?<=index =).{0,100}?(?=\\*)";
        private final String whenTimeRegx ="(?<=unitCount\\+).{0,100}?(?=;)";
//        private final String classroomRegx;
//        private final String weeksRegx;
//        private final String classTimeRegx;

        List<SustCourse> doResolve(){
            List<SustCourse> sustCourseList = new ArrayList<>();
            // 获取全部js代码
            String jsCourse = execRegxGroup0(courseHtml,jsCourseRegx);
            // 按照课程切分
            List<String> courses = execRegxGroups(jsCourse, oneCourseRegx);
            courses.forEach(k->{
                SustCourse sustCourse = new SustCourse();
                sustCourse.setTeacherName(execRegxGroup0(k, teacherNameRegx));
                // 其他信息
                String courseInfos = execRegxGroup0(k, courseInfoRegx);
                String[] split = courseInfos.split(",");
                sustCourse.setCourseName(split[1].substring(1,split[1].indexOf("(")));
                sustCourse.setClassroom(split[3].substring(1,split[3].lastIndexOf("\"")));
                sustCourse.setWeeks(split[4].substring(1,21));
                // 获取上课时间
                int day = new Integer(execRegxGroup0(k, dayTimeRegx));
                int when = new Integer(execRegxGroup0(k, whenTimeRegx));
                // +1是为了不让下标从0开始
                sustCourse.setClassTime(day*11+when+1);
                sustCourseList.add(sustCourse);
            });
            return sustCourseList;
        }

        private String execRegxGroup0(String content,String regx){
            String res="";
            // 正则匹配
            Matcher matcher = Pattern.compile(regx, Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                res= matcher.group();
            }
            return res;
        }
        private List<String> execRegxGroups(String content,String regx){
            List<String> lists = new ArrayList<>();
            // 正则匹配
            Matcher matcher = Pattern.compile(regx, Pattern.DOTALL).matcher(content);
            while(matcher.find()){
                lists.add(matcher.group());
            }
            return lists;
        }

    }

    public CourseResolve getCourseResolve() {
        return courseResolve;
    }
}
