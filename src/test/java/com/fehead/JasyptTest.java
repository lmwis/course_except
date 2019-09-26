package com.fehead;

import com.fehead.compoment.NoClassGenerator;
import com.fehead.controller.vo.NoCourse4MutUsers;
import com.fehead.dao.CourseMapper;
import com.fehead.dao.UserMapper;
import com.fehead.dao.entity.Course;
import com.fehead.dao.entity.NoCourse4Group;
import com.fehead.dao.entity.NoCoursePack;
import com.fehead.service.impl.CourseServiceImpl;
import com.fehead.utils.RandomUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Unit test for simple App.
 */


public class JasyptTest {


    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("fehead");                        // 加密的密钥
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "mysqladmin";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("fehead");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "n85jwNXPx3zSAlGUS3MLw6aR0ck3t25i";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }


}
