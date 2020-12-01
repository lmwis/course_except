package com.fehead.course.auto;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import com.fehead.course.compoment.UserClassAutoImport;
import com.fehead.course.compoment.model.SustCourse;
import com.fehead.lang.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 课表相关类测试
 * @Author: lmwis
 * @Date 2020-11-30 19:32
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserClassTest {

    @Autowired
    UserClassAutoImport userClassAutoImport;

    @Test
    public void resolveLoginHtmlTest(){
        String result1= HttpUtil.get("http://login.sust.edu.cn/cas/login?service=http%3A%2F%2Fmy.sust.edu.cn%2Fc%2Fportal%2Flogin");
        String regex = "(?<=<input type=\"hidden\" name=\"execution\" value=\").*?(?=\")";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(result1);
        if (matcher.find()) {
            String result= matcher.group();
            System.out.println(result);
        }
    }

    @Test
    public void doGetUserClass() throws BusinessException {
        userClassAutoImport.prepareCASLogin("201806020527", "conanyuan1.")
                .doResolve().forEach(System.out::println);
    }

}
