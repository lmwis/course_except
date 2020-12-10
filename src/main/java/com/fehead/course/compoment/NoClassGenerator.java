package com.fehead.course.compoment;

import com.fehead.course.controller.CourseController;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.NoCourse;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author lmwis
 * @description: 无课表生成器
 * @date 2019-09-10 22:23
 * @Version 1.0
 */
@Component
@MapperScan("{com.fehead.course.dao}")
public class NoClassGenerator {

    public NoClassGenerator() {
        allClassInit(); //初始化
    }

    Set<String> courseStrId = new HashSet<>();

    public static int TOTAL_WEEKS_NUM = 20;

    public Set<String> getCourseStrId() {
        return courseStrId;
    }

    public void setCourseStrId(Set<String> courseStrId) {
        this.courseStrId = courseStrId;
    }

//    public Course transToObject(String strId){
//
//        Course course = new Course();
//        String weeks =strId.substring(0,2);
//        String week = strId.substring(2,3);
//        String period = strId.substring(3,4);
//        String convertWeeks = convertWeeks(new Integer(weeks));
//        String convertWeek = convertWeek(new Integer(week));
//        int convertPeriod = convertPeriod(new Integer(period));
//        course.setWeeks(convertWeeks);
//        course.setWeek(convertWeek);
//        course.setPeriod(convertPeriod);
//        return course;
//
//    }

    /**
     * id转化为对象
     * @param strId id
     * @param id 用户id，因此对象转为id会擦除用户id，所以转化回来需要额外设置
     * @return
     */
    public Set<Course> transToObject(Set<String> strId,long id) {
        Set<Course> courses = new HashSet<>();
        for (String s : strId) {
            Course course = new Course();
            String weeks = s.substring(0, 2);
            String week = s.substring(2, 3);
            String period = s.substring(3, 4);
            String convertWeeks = convertWeeks(new Integer(weeks) - 1);
            String convertWeek = convertWeek(new Integer(week) - 1);
            int convertPeriod = convertPeriod(new Integer(period) - 1);
            course.setWeeks(convertWeeks);
            course.setWeek(convertWeek);
            course.setPeriod(convertPeriod);
            course.setId(id);
            courses.add(course);
        }

        return courses;

    }

    /**
     * 同课进行周次合并
     *
     * @param courses
     * @return
     */
    public Set<Course> obtainCourse(Collection<Course> courses) {
        Set<Course> courseRes = new HashSet<>();


        return courseRes;
    }


    /**
     * 课程初始化
     */
    public void allClassInit() {
        List<Course> courses = generateAllClass();
        for (Course c : courses) {
            courseStrId.add(transNum(c));
        }
    }

    /**
     * 课程集合转化为课程id集合
     *
     * @param courseList
     * @return
     */
    public Set<String> transNum(List<Course> courseList) {
        Set<String> courseStr = new HashSet<>();
        for (Course course : courseList) {
            String s = transNum(course);
            courseStr.add(s);
        }
        return courseStr;
    }

    /**
     * 按周次分割课程
     * 划分为单元课
     *
     * @param course
     * @return
     */
    public List<Course> splitWeeksCourse(List<Course> course) {
        List<Course> courses = new ArrayList<>();
        for (Course c : course) {
            List<String> strings = splitWeeks(c.getWeeks());
            for (String s : strings) {
                Course newCourse = packCourse(c, s);

                courses.add(newCourse);
            }
        }

        return courses;
    }

    private Course packCourse(Course course, String weeks) {
        Course newCourse = new Course();
        newCourse.setPeriod(course.getPeriod());
        newCourse.setWeek(course.getWeek());
        newCourse.setUserId(course.getUserId());
        newCourse.setWeeks(weeks);

        return newCourse;
    }

    /**
     * 按周次分割课程
     *
     * @param course
     * @return
     */
    public List<Course> splitWeeksCourse(Course course) {
        List<Course> courses = new ArrayList<>();
        List<String> strings = splitWeeks(course.getWeeks());
        for (String s : strings) {
            Course newCourse = packCourse(course, s);
            courses.add(newCourse);
        }
        return courses;
    }

    /**
     * 将周次分割出来
     *
     * @param weeks
     * @return
     */
    public List<String> splitWeeks(String weeks) {
        List<String> courseStr = new ArrayList<>();

        for (int i = 0; i < weeks.toCharArray().length; i++) {
            if (weeks.charAt(i) == '1') {
                String str = CourseController.weekTemp;
                StringBuffer stringBuffer = new StringBuffer(str);
                stringBuffer.setCharAt(i, '1');
                courseStr.add(stringBuffer.toString());
            }
        }

        return courseStr;
    }

    /**
     * 将课程按照逻辑转化为唯一id
     * 0853  第八周，周五，第56节
     *
     * @param course course
     * @return String
     */
    public String transNum(NoCourse course) {

        StringBuffer strId = new StringBuffer();

        String transWeeks = transWeeks(course.getWeeks());
        String transWeek = transWeek(course.getWeek());
        String transPeriod = transPeriod(course.getPeriod());
        strId.append(transWeeks);
        strId.append(transWeek);
        strId.append(transPeriod);
        return strId.toString();

    }

    /**
     * 周次转id
     *
     * @param weeks 0001000000000000
     * @return
     */
    public String transWeeks(String weeks) {
        String num = "";
        int i = weeks.indexOf("1") + 1;
        if (i < 10) {
            num = "0" + i;
        } else {
            num = String.valueOf(i);
        }

        return num;
    }

    /**
     * 周转id
     *
     * @param week
     * @return
     */
    public String transWeek(String week) {

        switch (week) {
            case "周一":
                return "1";
            case "周二":
                return "2";
            case "周三":
                return "3";
            case "周四":
                return "4";
            case "周五":
                return "5";
            case "周六":
                return "6";
            case "周日":
                return "7";
        }
        return "0";

    }

    /**
     * 节数转id
     *
     * @param period
     * @return
     */
    public String transPeriod(int period) {
        switch (period) {
            case 12:
                return "1";
            case 34:
                return "2";
            case 56:
                return "3";
            case 78:
                return "4";
            case 910:
                return "5";
            case 11:
                return "6";
        }
        return "0";
    }

    /**
     * 生成所有的课单元
     *
     * @return
     */
    public List<Course> generateAllClass() {

        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 20; i++) { // 第一周到第二十周
            for (int j = 0; j < 7; j++) { // 一周七天
                for (int k = 0; k < 6; k++) { // 一天7节课
                    Course course = new Course();
                    course.setPeriod(convertPeriod(k));
                    course.setWeek(convertWeek(j));
                    course.setWeeks(convertWeeks(i));

                    courses.add(course);
                }
            }
        }
        return courses;
    }

    /**
     * 第几节转显示节数
     * 0-5
     *
     * @param num
     * @return
     */
    public int convertPeriod(int num) {
        switch (num) {
            case 0:
                return 12;
            case 1:
                return 34;
            case 2:
                return 56;
            case 3:
                return 78;
            case 4:
                return 910;
            case 5:
                return 11;
                default:
                    return 0;
        }

    }

    /**
     * 周几转文字形式
     * 0-6
     *
     * @param num
     * @return
     */
    public String convertWeek(int num) {
        switch (num) {
            case 0:
                return "周一";
            case 1:
                return "周二";
            case 2:
                return "周三";
            case 3:
                return "周四";
            case 4:
                return "周五";
            case 5:
                return "周六";
            case 6:
                return "周日";
                default:
                    return "";

        }
    }

    /**
     * 周次转统一形式
     * 2->00100000000000000000
     *
     * @param num
     * @return
     */
    public String convertWeeks(int num) {
        StringBuilder weeks = new StringBuilder(CourseController.weekTemp);
//        weeks.
        weeks.setCharAt(num, '1');

        return weeks.toString();
    }

    /**
     * 判断指定周次是否在统一形式的字符串中为1
     *
     * @param weeks
     * @param weeksNum
     * @return
     */
    public boolean equalWeeks(String weeks, int weeksNum) {
        char c = weeks.charAt(weeksNum - 1);
        if (c == '1') {
            return true;
        }
        return false;
    }

    /**
     * 生成单元无课表
     * @param userCourses 用户课表
     * @return 单元无课表
     */
    public Collection<Course> generateNoClass(long userId,List<Course> userCourses) {

//        List<Course> userCourses = courseMapper.selectByUserId(userId);
        // 划分为单元课程
        List<Course> unitCourse = this.splitWeeksCourse(userCourses);
        Set<String> courseStrId = this.getCourseStrId();
        // 课程id结果集
        Set<String> resCourseStrId = new HashSet<>();
        // 一学期二十周所有的单元课程
        List<Course> courses = this.generateAllClass();
        for (Course c : courses) {
            resCourseStrId.add(this.transNum(c));
        }
        for (Course c : unitCourse) {
            String s = this.transNum(c);
            if (courseStrId.contains(s)) {// 课程是否存在
                resCourseStrId.remove(s);
            }
        }

        return this.transToObject(resCourseStrId,userId);
    }

    /**
     * 将01形式的weeks转换为weeks文本
     * exp:10100111111111000011 -> 1,3,6-14,19-20
     * 00011111100000000000 ->1-6
     *
     * @param weeks
     * @return
     */
    public String convertWeeksTestFromWeeks(String weeks) {
        int start = 1;
        String text = "";
        boolean flag1 = false;
        boolean flag2 = false;
        for (int i = 0; i < TOTAL_WEEKS_NUM; i++) {
            if (weeks.charAt(i) == '0') {
                flag1 = true;
            }
            if (weeks.charAt(i) == '1') {
                flag2 = true;
            }
        }
        if (!flag1) { // 表示全部为1
            return "1-20";
        }
        if (!flag2) { // 表示全部为0
            return "0";
        }
        for (int i = 0; i < TOTAL_WEEKS_NUM; i++) {
            char c = weeks.charAt(i);
            if (i >= 1 && c == '0' && (weeks.charAt(i - 1) == '1')) { // 当前为0，上一个为1才有效
                text += (start == i ? start : start + "-" + i) + ","; // 相同如：1-1 则简化为1
            }
            if (i >= 1 && c == '1' && (weeks.charAt(i - 1) == '0')) { // 当前为1，上一个为0才有效，移动头指针
                start = i + 1;
            }

            if (i == TOTAL_WEEKS_NUM - 1 && c=='1') { // 扫描到最后若以1结尾
                text += (start == TOTAL_WEEKS_NUM ? start : start + "-" + TOTAL_WEEKS_NUM) + ",";;
            }
        }

        return text.substring(0, text.length() - 1);
    }


}
