package com.fehead.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.course.compoment.NoClassGenerator;
import com.fehead.course.compoment.UserGeneratorNoClassTask;
import com.fehead.course.dao.CourseMapper;
import com.fehead.course.dao.UserMapper;
import com.fehead.course.dao.entity.Course;
import com.fehead.course.dao.entity.NoCourse4Group;
import com.fehead.course.dao.entity.NoCoursePack;
import com.fehead.course.dao.entity.SustCourse;
import com.fehead.course.error.EmCourseExceptError;
import com.fehead.course.service.CourseService;
import com.fehead.course.service.model.AddFormArr;
import com.fehead.course.service.model.UpdateFormArr;
import com.fehead.course.validate.CourseValidator;
import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lmwis
 * @description: 处理课程信息相关的请求
 * @date 2019-09-02 11:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController {

    public static final String weekTemp = "00000000000000000000"; // 0表示没课，1表示有课

    public static final String weekTempFull = "11111111111111111111"; // 0表示没课，1表示有课

    private String weeksCount = weekTemp;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseService courseService;

    @Autowired
    NoClassGenerator noClassGenerator;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserGeneratorNoClassTask userGeneratorNoClassTask;

    @Autowired
    GroupController groupController;

    @Autowired
    CourseValidator courseValidator; // 课程校验器

    /**
     * 创建新的课程单元
     *
     * @param userId
     * @param forms
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @PostMapping()
    @ApiOperation("创建一个课程单元 exp: week(周一) period(1) weeks(11111111110001111000)")
    public FeheadResponse createCourse(@RequestParam("user_id") String userId, @RequestParam("add_forms_arr") String forms) throws BusinessException, IOException {

        AddFormArr[] addFormArrs = objectMapper.readValue(forms, AddFormArr[].class);
        if (!validateNull(forms, userId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmCourseExceptError.SUST_JWC_LOGIN_FAIL);
        }

        for (int i = 0; i < addFormArrs.length; i++) {
            String weeks = addFormArrs[0].getWeeks();
            String week = addFormArrs[0].getWeek();
            int period = addFormArrs[0].getPeriod();

//            String weeksText = addFormArrs[0].getWeeks_text();

            // weeks校验
            if (courseValidator.validateWeeks(weeks)) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "周次不合法");
            }
            // period校验
            if (courseValidator.validatePeriod(period)) {// 节数格式不合法
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "节数格式不合法");
            }
            // 星期校验
            if (courseValidator.validateWeek(week)) {// 节数格式不合法
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "星期不合法");
            }

            String weeksText = noClassGenerator.convertWeeksTestFromWeeks(weeks); // 将weeks转化为文本形式


            // 数据校验
            if (!validateNull(weeks, week, period, weeksText)) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }

            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("week", week);
            queryWrapper.eq("weeks", weeks);
            queryWrapper.eq("period", period);
            Course courseInSql = courseMapper.selectOne(queryWrapper);
            if (courseInSql != null) { // 课程已经添加过了
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST, "请勿重复添加相同的课程");
            }
            Course course = new Course();
            course.setUserId(new Integer(userId));
            course.setWeek(week);
            course.setPeriod(new Integer(period));
            course.setWeeks(weeks);
            course.setWeeksText(weeksText);

            // 线程保护 写入数据库
            synchronized (CourseController.class) {
                courseMapper.insert(course);
            }
        }

        // 生成无课表
        // 异步执行
        userGeneratorNoClassTask.noClassAction(Long.valueOf(userId));

        return CommonReturnType.create(null);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{course_id}")
    @ApiOperation("根据id删除一个课程单元")
    public FeheadResponse deleteCourse(@PathVariable("course_id") int id) {

        courseMapper.deleteById(id);

        return CommonReturnType.create(null);
    }

    /**
     * 查询用户所有的课
     *
     * @param userId
     * @return
     * @throws BusinessException
     */
    @GetMapping
    @ApiOperation("查询用户所有的课")
    public FeheadResponse getAllCourse(@RequestParam("user_id") int userId) throws BusinessException {

        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        List<Course> courses = courseService.selectByUserId(userId);

        return CommonReturnType.create(courses);

    }
    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping("/{course_id}")
    @ApiOperation("根据id获取单元课程信息")
    public FeheadResponse getCourseById(@PathVariable("course_id") int id) throws BusinessException {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(EmBusinessError.DATARESOURCE_CONNECT_FAILURE, "数据不存在");
        }
        return CommonReturnType.create(course);

    }

    /**
     * 修改
     *
     * @param userId
     * @param forms
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @PutMapping()
    @ApiOperation("修改单元课程信息")
    public FeheadResponse updateCourse(@RequestParam("user_id") String userId, @RequestParam("edit_forms_arr") String forms) throws BusinessException, IOException {


//        courseMapper.delete(queryWrapper);
        // 转化
        UpdateFormArr[] updateFormArrs = objectMapper.readValue(forms, UpdateFormArr[].class);
        List<UpdateFormArr> createForms = new ArrayList<>();
        for (UpdateFormArr updateFormArr : updateFormArrs) {

            String weeks = updateFormArr.getWeeks();
            String week = updateFormArr.getWeek();
            int period = updateFormArr.getPeriod();
            String weeksText = updateFormArr.getWeeks_text();
            if (updateFormArr.getId() <= 0) { //id小于0是需要添加的
                // weeks校验
                if (courseValidator.validateWeeks(weeks)) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "周次不合法");
                }
                // 数据校验
                if (!validateNull(weeks, week, period, weeksText)) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
                }
                createForms.add(updateFormArr);
            } else if (StringUtils.equals(updateFormArr.getWeeks_text(), "delete")) { // 是需要删除的
                courseMapper.deleteById(updateFormArr.getId());
            } else { //需要修改的

                // 重新转化weeks_text
                weeksText = noClassGenerator.convertWeeksTestFromWeeks(weeks);

                // weeks校验
                if (courseValidator.validateWeeks(weeks)) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "周次不合法");
                }
                // 数据校验
                if (!validateNull(weeks, week, period, weeksText)) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
                }

                // period校验
                if (courseValidator.validatePeriod(period)) {// 节数格式不合法
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "节数格式不合法");
                }

                // 星期校验
                if (courseValidator.validateWeek(week)) {// 节数格式不合法
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "星期不合法");
                }

                Course course = new Course();
                course.setId(updateFormArr.getId());
                course.setUserId(new Integer(userId));
                course.setWeeks(weeks);
                course.setWeek(week);
                course.setPeriod(period);
                course.setWeeksText(weeksText);

                courseMapper.updateById(course);
            }
        }
        if (createForms.size() > 0) {
            String s = objectMapper.writeValueAsString(createForms);
            // 创建课程
            createCourse(userId, s);
        }


        // 生成无课表
        // 异步执行
        userGeneratorNoClassTask.noClassAction(Long.valueOf(userId));

        return CommonReturnType.create("修改成功");
    }

    @GetMapping("/user/{user_id}/week}")
    @ApiOperation("获取用户某星期的课程")
    public FeheadResponse getCourseByWeek(@PathVariable("user_id") int userId, @RequestParam("week") String week) throws BusinessException {

        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        List<Course> courses = courseMapper.selectByUserIdAndWeek(userId, week);

        return CommonReturnType.create(courses);
    }

    @GetMapping("/user/{user_id}/weeks}")
    @ApiOperation("获取用户某一周的课程")
    public FeheadResponse getCourseByWeeks(@PathVariable("user_id") int userId, @RequestParam("weeks") int weeks) throws BusinessException {

        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        List<Course> courses = courseService.selectByUserIdAndWeeks(userId, weeks);

        return CommonReturnType.create(courses);
    }

    /**
     * 获取某用户的无课表
     *
     * @param userId
     * @return
     */
    @GetMapping("/no_class/{user_id}")
    @ApiOperation("获取用户的无课表")
    public FeheadResponse getNoClass(@PathVariable("user_id") int userId) {

        Collection<NoCoursePack> courses = courseService.getUserNoClassPack(userId);

        return CommonReturnType.create(courses);

    }

    /**
     * 获取部门无课表
     *
     * @param groupId
     * @param weeks
     * @param include
     * @return
     */
    @GetMapping("/no_class/group/{group_id}")
    @ApiOperation("获取部门的无课表")
    public FeheadResponse getGroupNoClass(@RequestParam("user_id") int userId
            , @PathVariable("group_id") int groupId
            , @RequestParam(value = "weeks", required = false, defaultValue = "1") int weeks
            , @RequestParam(value = "include", required = false, defaultValue = "0") @ApiParam(value = "非必要参数，是否需要包含部门组织者的课表：需要为1，不需要为0") int include) throws BusinessException {

        groupController.groupActionValidate(userId, groupId);

        Collection<NoCourse4Group> courses = null;
        if (include == 1) {
            courses = courseService.getGroupNoClassPackOrderByWeeksInclude(groupId, weeks);
        } else {
            courses = courseService.getGroupNoClassPackOrderByWeeks(groupId, weeks);
        }


        return CommonReturnType.create(courseService.packNoClass4Group(courses));
    }

    /**
     * 生成用户无课表
     *
     * @return
     */
    @GetMapping("/no_class/generate/{user_id}")
    @ApiOperation("生成用户无课表")
    public FeheadResponse generateNoClass(@PathVariable("user_id") int userId) throws BusinessException {

        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        noClassGenerator.generateNoClass(userId);

        return CommonReturnType.create(null);
    }

    /**
     * 导入教务处课表并生成无课表，无课程详细信息
     * @return 课程列表
     */
    @PostMapping("/generate/auto/{user_id}")
    @ApiOperation("根据教务处课表自动获取用户课表(原来课表类型)，并异步生成无课表")
    public FeheadResponse generateCourseAuto(@PathVariable("user_id") int userId,@ApiParam("教务系统登录账号") String username
            ,@ApiParam("教务系统登录密码")String password) throws BusinessException {
        validateNull(username,password);
        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        List<Course> courseList = courseService.getUserCourseFromSust(username,password);

        return CommonReturnType.create(courseList);
    }

    @PostMapping("/generate/auto/new/{user_id}")
    @ApiOperation("根据教务处课表自动获取用户课表(新课表类型)，并异步生成无课表")
    public FeheadResponse generateNewTypeCourseAuto(@PathVariable("user_id") int userId,@ApiParam("教务系统登录账号") String username
            ,@ApiParam("教务系统登录密码")String password) throws BusinessException {
        validateNull(username,password);
        if (userMapper.selectById(userId) == null) { // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        List<SustCourse> courseList = courseService.getUserCourseFromSustNewType(username,password);
        return CommonReturnType.create(courseList);
    }
}
