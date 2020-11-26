package com.fehead.course.validate;

<<<<<<< HEAD:src/main/java/com/fehead/course/validate/CourseValidator.java
import com.fehead.course.compoment.NoClassGenerator;
=======
import com.fehead.compoment.NoClassGenerator;
>>>>>>> 392b3d47804a5648fb64381f803441ac77adadd3:src/main/java/com/fehead/validate/CourseValidator.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 课表格式校验器
 * @Author lmwis
 * @Date 2019-10-20 17:39
 * @Version 1.0
 */
@Component
public class CourseValidator {

    @Autowired
    NoClassGenerator noClassGenerator;

    /**
     * 节数校验，只能为固定枚举值
     *  12，34，56，78，910，11
     * @param period
     * @return
     */
    public boolean validatePeriod(int period){
        if (Integer.parseInt(noClassGenerator.transPeriod(period)) == 0) { // 节数格式不合法
            return true;
        }
        return false;
    }

    /**
     * 周次校验，只能为固定长度
     *  20
     * @param weeks
     * @return
     */
    public boolean validateWeeks(String weeks){
        char[] chars = weeks.toCharArray();
        if (chars.length != 20) { // 长度必须为20
            return true;
        }
        return false;
    }

    /**
     * 周校验，只能为固定枚举值
     *  周一，周二，周三，周四，周五，周六，周日
     * @param week
     * @return
     */
    public boolean validateWeek(String week){
        // 星期校验
        if (Integer.parseInt(noClassGenerator.transWeek(week)) == 0) { // 周不合法
            return true;
        }
        return false;
    }

}
