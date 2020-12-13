package com.fehead.course.service;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-13 11:24
 * @Version 1.0
 */
public class CourseServiceTest {
    private static final GregorianCalendar startDate=new GregorianCalendar(2020, Calendar.SEPTEMBER,7);

    @Test
    public void whenGetWeeks(){
        long nowTime = new GregorianCalendar(2020, Calendar.DECEMBER,14).getTime().getTime();
        long startTime = startDate.getTime().getTime();
        // 相差天数
        int days = (int)((nowTime - startTime) / 1000 / 60 / 60 / 24);
        System.out.println("days:"+days);
        System.out.println("周次："+(days/7+1));
    }
}
