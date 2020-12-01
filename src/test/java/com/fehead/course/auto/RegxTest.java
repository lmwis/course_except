package com.fehead.course.auto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-01 21:52
 * @Version 1.0
 */
public class RegxTest {

    @Test
    public void GroupsTest(){
        String content = "var teachers = [{id:1968,name:\"熊静\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1968,name:\"熊静\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"11211(4109460.01)\",\"计算机网络原理(4109460.01)\",\"281\",\"一号教学楼D403\",\"01101111111100000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =2*unitCount+0;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =2*unitCount+1;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1968,name:\"熊静\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1968,name:\"熊静\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"11211(4109460.01)\",\"计算机网络原理(4109460.01)\",\"281\",\"一号教学楼D403\",\"01100111110100000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =0*unitCount+4;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =0*unitCount+5;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1108,name:\"王立梅\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1108,name:\"王立梅\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"3950(2109413.03)\",\"微机原理与接口技术(2109413.03)\",\"284\",\"一号教学楼D406\",\"01101111111111000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =4*unitCount+2;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =4*unitCount+3;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1108,name:\"王立梅\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1108,name:\"王立梅\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"3950(2109413.03)\",\"微机原理与接口技术(2109413.03)\",\"277\",\"一号教学楼D306\",\"01101111111101000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =1*unitCount+2;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =1*unitCount+3;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1180,name:\"姚斌\",lab:false},{id:5064,name:\"李婉\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:5064,name:\"李婉\",lab:false},{id:1180,name:\"姚斌\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"3953(2109416.01)\",\"离散数学（2）(2109416.01)\",\"281\",\"一号教学楼D403\",\"01101111111101000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =2*unitCount+2;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =2*unitCount+3;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:560,name:\"田延安\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:560,name:\"田延安\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"6199(3209463.01)\",\"面向对象C++(3209463.01)\",\"280\",\"一号教学楼D402\",\"01101111111111000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =3*unitCount+4;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =3*unitCount+5;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1707,name:\"齐勇\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1707,name:\"齐勇\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"11218(3109441.01)\",\"算法分析与设计(3109441.01)\",\"275\",\"一号教学楼D304\",\"01101111011101110000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =1*unitCount+0;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =1*unitCount+1;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:944,name:\"杨悦欣\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:944,name:\"杨悦欣\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"5015(3109431.01)\",\"面向对象Java(3109431.01)\",\"281\",\"一号教学楼D403\",\"01101111111101111100000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =0*unitCount+6;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =0*unitCount+7;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:5065,name:\"丁磊\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:5065,name:\"丁磊\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"11213(4109462.01)\",\"高级操作系统Linux(4109462.01)\",\"280\",\"一号教学楼D402\",\"01101111111111111100000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =3*unitCount+6;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =3*unitCount+7;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:920,name:\"白清平\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:920,name:\"白清平\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"2869(11313005.09)\",\"形势与政策教育（5）(11313005.09)\",\"226\",\"一号教学楼BC01\",\"00000001100000000000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =4*unitCount+4;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =4*unitCount+5;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\tvar teachers = [{id:1906,name:\"周璐\",lab:false}];\n" +
                "\t\tvar actTeachers = [{id:1906,name:\"周璐\",lab:false}];;\n" +
                "\t\tvar assistant = _.filter(actTeachers, function(actTeacher) {\n" +
                "\t\t\treturn (_.where(teachers, {id:actTeacher.id,name:actTeacher.name,lab:actTeacher.lab}).length == 0) && (actTeacher.lab == true);\n" +
                "\t\t});\n" +
                "\t\tvar assistantName = \"\";\n" +
                "\t\tif (assistant.length > 0) {\n" +
                "\t\t\tassistantName = assistant[0].name;\n" +
                "\t\t\tactTeachers = _.reject(actTeachers, function(actTeacher) {\n" +
                "\t\t\t\treturn _.where(assistant, {id:actTeacher.id}).length > 0;\n" +
                "\t\t\t});\n" +
                "\t\t}\n" +
                "\t\tvar actTeacherId = [];\n" +
                "\t\tvar actTeacherName = [];\n" +
                "\t\tfor (var i = 0; i < actTeachers.length; i++) {\n" +
                "\t\t\tactTeacherId.push(actTeachers[i].id);\n" +
                "\t\t\tactTeacherName.push(actTeachers[i].name);\n" +
                "\t\t}\n" +
                "\t\t\tactivity = new TaskActivity(actTeacherId.join(','),actTeacherName.join(','),\"2942(1114103.09)\",\"职业生涯规划及就业指导（3）(1114103.09)\",\"293\",\"一号教学楼DE01\",\"00000000000001100000000000000000000000000000000000000\",null,null,assistantName,\"\",\"\");\n" +
                "\t\t\tindex =4*unitCount+6;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=activity;\n" +
                "\t\t\tindex =4*unitCount+7;\n" +
                "\t\t\ttable0.activities[index][table0.activities[index].length]=";

        String oneCourseRegx = "var teachers[\\s\\S]*?table0";

        List<String> lists = new ArrayList<>();
        // 正则匹配
        Matcher matcher = Pattern.compile(oneCourseRegx, Pattern.DOTALL).matcher(content);
//        int count = matcher.groupCount();
        while(matcher.find()){
            lists.add(matcher.group());
        }

        System.out.println(lists.size());

    }
}
