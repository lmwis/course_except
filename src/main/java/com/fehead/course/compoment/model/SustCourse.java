package com.fehead.course.compoment.model;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: 陕科大课程类型
 * var actTeacherId = [];
 * 		var actTeacherName = [];
 * 		for (var i = 0; i < actTeachers.length; i++) {
 * 			actTeacherId.push(actTeachers[i].id);
 * 			actTeacherName.push(actTeachers[i].name);
 *                }
 * 			activity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),"11158(21311005.16)"
 * 			,"线性代数(21311005.16)","348","一号教学楼F201(401F201)","00001111111111111111000000000000000000000000000000000"
 * 			,null,null,assistantName,"","");
 * 			index =2*unitCount+4;
 * 			table0.activities[index][table0.activities[index].length]=activity;
 * 			index =2*unitCount+5;
 * 			table0.activities[index][table0.activities[index].length]=activity;
 * 		var teachers = [{id:686,name:"彭卫丽",lab:false}];
 * 		var actTeachers = [{id:686,name:"彭卫丽",lab:false}];;
 * 		var assistant = _.filter(actTeachers, function(actTeacher) {
 * 			return (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) &&
 * 			(actTeacher.lab == true);
 *        });
 * 		var assistantName = "";
 * 		if (assistant.length > 0) {
 * 			assistantName = assistant[0].name;
 * 			actTeachers = _.reject(actTeachers, function(actTeacher) {
 * 				return _.where(assistant, {id:actTeacher.id}).length > 0;
 *            });
 *        }
 * @Author: lmwis
 * @Date 2020-12-01 18:44
 * @Version 1.0
 */
@Data
@ToString
public class SustCourse {

    private long id;

    /**
     * 课程姓名
     */
    private String courseName;

    /**
     * 老师姓名
     */
    private String teacherName;

    /**
     * 教室地点
     */
    private String classroom;

    /**
     * 上课周次
     */
    private String weeks;

    /**
     * 上课时间：周几第几节
     */
    private int classTime;

}
