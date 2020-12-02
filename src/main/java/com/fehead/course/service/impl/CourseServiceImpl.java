package com.fehead.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fehead.course.compoment.NoClassGenerator;
import com.fehead.course.compoment.UserClassAutoImport;
import com.fehead.course.compoment.model.SustCourse;
import com.fehead.course.controller.CourseController;
import com.fehead.course.controller.vo.NoCourse4MutUsers;
import com.fehead.course.dao.CourseMapper;
import com.fehead.course.dao.GroupMapper;
import com.fehead.course.dao.NoCourseMapper;
import com.fehead.course.dao.NoCoursePackMapper;
import com.fehead.course.dao.entity.*;
import com.fehead.course.service.CourseService;
import com.fehead.lang.error.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * @author lmwis
 * @description: Course服务实现类
 * @date 2019-09-07 11:45
 * @Version 1.0
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    NoClassGenerator noClassGenerator;

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    NoCoursePackMapper noCoursePackMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    NoCourseMapper noCourseMapper;

    @Autowired
    UserClassAutoImport userClassAutoImport;

    @Override
    public List<Course> selectByUserId(int userId) {

        List<Course> courses = courseMapper.selectByUserId(userId);

        return courses;
    }

    @Override
    public List<Course> selectByUserIdAndWeeks(int userId, int weeks) {

        // 先查到内存中，然后在内存中进行处理

        // 查出所有课程
        List<Course> courses = selectByUserId(userId);
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (judgeWeeksTempAnd(course.getWeeks(), weeks)) { //周次匹配成功
                result.add(course);
            }
        }

        return result;
    }

    /**
     * 未完成
     * @param weeks
     * @param weeksNum
     * @return
     */
    public boolean judgeWeeksTempAnd(String weeks, int weeksNum) {
        char c = weeks.charAt(weeksNum);
        if (c == '1') { //为匹配成功
            return true;
        }
        return false;
    }




    /**
     * 获取封装好的无课表
     *  从原始数据中进行封装
     *  之后需要改成直接从封装好的数据库中查询
     * @param userId
     * @return
     */
    @Override
    public Collection<NoCoursePack> getUserNoClassPack(long userId) {

        List<NoCoursePack> resNoCourse = new ArrayList<>();

        // 所有的课程
        List<NoCourse> noCourses = noCourseMapper.selectListDistinct(userId);
        for (NoCourse noCourse : noCourses) { // 循环所有的无课表单元
            QueryWrapper<NoCourse> queryWrapper = new QueryWrapper();
            queryWrapper.allEq(new HashMap<String, String>() {{
                put("user_id", String.valueOf(userId));
                put("period", String.valueOf(noCourse.getPeriod()));
                put("week", noCourse.getWeek());
            }});
            List<NoCourse> noCoursesWeeks = noCourseMapper.selectList(queryWrapper);
            StringBuffer newWeeks = new StringBuffer(CourseController.weekTemp);
            for (int i = 0; i < 20; i++) {
                for (NoCourse noCoursesWeek : noCoursesWeeks) {
                    if (noCoursesWeek.getWeeks().charAt(i) == '1') {
                        newWeeks.setCharAt(i, '1');
                    }
                }
            }
            // 封装
            resNoCourse.add(new NoCoursePack() {{
                setUserId(userId);
                setWeeks(newWeeks.toString());
                setPeriod(noCourse.getPeriod());
                setWeek(noCourse.getWeek());
            }});

        }

        return resNoCourse;
    }
    /**
     * 获取封装好的无课表
     *  从封装好的数据库中查询
     * @param userId
     * @return
     */
    public Collection<NoCoursePack> getUserNoClassPackFromDB(long userId) {

        QueryWrapper<NoCoursePack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<NoCoursePack> noCoursePacks = noCoursePackMapper.selectList(queryWrapper);

        return noCoursePacks;

    }


    /**
     * 获取部门的无课表
     *  指定周次 exp:第1周
     * @param groupId
     * @param weeks
     * @return
     */
    @Override
    public Collection<NoCourse4Group> getGroupNoClassPackOrderByWeeks(int groupId,int weeks) {

        Collection<NoCourse4Group> userAllNoClassPack = new HashSet<>();

        // 获取组织内所有的用户 不包括组织创建者
        List<User> users = groupMapper.selectAllUsersByGroupId(groupId);
        // 获取数据
        userAllNoClassPack.addAll(getUserGroupNoClassPackOrderByWeeks(users,weeks));

        return userAllNoClassPack;
    }

    /**
     * 获取一群用户的无课表
     *  指定周次 exp:第1周
     * @param users
     * @param weeks
     * @return
     */
    private Collection<NoCourse4Group> getUserGroupNoClassPackOrderByWeeks(List<User> users,int weeks){
        Collection<NoCourse4Group> userNoClassPack4Group = new HashSet<>();
        // 每个用户的课表都进行遍历
        for(User u : users){
            // 获取用户无课表
//            Collection<NoCoursePack> userNoClassPack = getUserNoClassPack(u.getId());
            // 从打包好的数据表中查询
            Collection<NoCoursePack> userNoClassPack = getUserNoClassPackFromDB(u.getId());
            for (NoCoursePack noCourse : userNoClassPack) {

//                if(!StringUtils.equals(noCourse.getWeek(),noClassGenerator.convertWeek(week-1))){
//                    continue;
//                }
                // 周次筛选
                if(!noClassGenerator.equalWeeks(noCourse.getWeeks(),weeks)){
                    continue;
                }

                NoCourse4Group noCourse4Group = new NoCourse4Group();
                noCourse4Group.setUsername(u.getNickname());

                // 因为强行转换失败所以采用赋值的方式
                // 原因不明
                BeanUtils.copyProperties(noCourse,noCourse4Group);
                userNoClassPack4Group.add(noCourse4Group);
            }
        }
        return userNoClassPack4Group;
    }

    /**
     * 获取部门的无课表 包括组织者
     * @param groupId
     * @param weeks
     * @return
     */
    @Override
    public Collection<NoCourse4Group> getGroupNoClassPackOrderByWeeksInclude(int groupId, int weeks) {

        Collection<NoCourse4Group> userAllNoClassPack = new HashSet<>();
        // 获取组织内所有的用户 不包括组织创建者
        List<User> users = groupMapper.selectAllUsersByGroupId(groupId);
        User user = groupMapper.selectCreatorInfo(groupId);
        // 添加组织创建者
        users.add(user);
        // 获取数据
        userAllNoClassPack.addAll(getUserGroupNoClassPackOrderByWeeks(users,weeks));
        return userAllNoClassPack;
    }

    /**
     * 组织无课表打包
     *  某一节课是那些人没课
     *
     * @param classes
     * @return
     */
    public Collection<NoCourse4MutUsers> packNoClass4Group(Collection<NoCourse4Group> classes){


        Collection<NoCourse4MutUsers> noClassPack4Group = new HashSet<>();

        // 课程对应
        Map<String,NoCourse4MutUsers> maps = new HashMap<>();

        // 存储出现了哪些课
        Set<String> courses = new HashSet();

        for (NoCourse4Group aClass : classes) {
            String s = noClassGenerator.transNum(aClass);
            s = s.substring(2);
            if(courses.contains(s)){ // 如果该节课存在
                NoCourse4MutUsers noCourse4MutUsers = maps.get(s);
                if(!noCourse4MutUsers.getUsername().contains(aClass.getUsername())){ // 如果姓名已经存在则不重复加入
                    noCourse4MutUsers.getUsername().add(aClass.getUsername());
                }
            }else {
                NoCourse4MutUsers noCourse4MutUsers = new NoCourse4MutUsers();
                BeanUtils.copyProperties(aClass,noCourse4MutUsers);
                noCourse4MutUsers.getUsername().add(aClass.getUsername());
                courses.add(s);
                maps.put(s,noCourse4MutUsers);
                noClassPack4Group.add(noCourse4MutUsers);
            }

        }

        return noClassPack4Group;
    }

    public void savePackNoClass(Collection<NoCoursePack> resNoCourse){
        resNoCourse.forEach(k->{
            NoCoursePack noCoursePack = new NoCoursePack();
            BeanUtils.copyProperties(k,noCoursePack);
            noCoursePackMapper.insert(noCoursePack);
        });
    }

    /**
     * 删除指定用户无课表
     * @param userId
     */
    @Override
    public void deleteUserNoClassPack(long userId) {
        QueryWrapper<NoCoursePack> noCoursePackQueryWrapper = new QueryWrapper<>();
        noCoursePackQueryWrapper.eq("user_id",userId);
        noCoursePackMapper.delete(noCoursePackQueryWrapper);
    }

    /**
     * 从教务处拉取
     * 封装为Course类型
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    @Override
    public List<Course> getUserCourseFromSust(String username, String password) throws BusinessException {
        List<SustCourse> sustCourseList = userClassAutoImport.prepareCASLogin(username, password).doResolve();
        List<Course> courseList = new ArrayList<>();
        sustCourseList.forEach(k-> courseList.add(convertFromSustCourse(k)));
        return courseList;
    }

    /**
     * 类型转化
     * @param sustCourse
     * @return
     */
    private Course convertFromSustCourse(SustCourse sustCourse){
        Course course = new Course();
        course.setWeek(sustCourse.getWeeks());
        return course;
    }
}
