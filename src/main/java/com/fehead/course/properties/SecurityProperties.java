package com.fehead.course.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/7/18 20:11
 */
@ConfigurationProperties(prefix = "fehead")
@Data
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private SmsProperties smsProperties = new SmsProperties();

    private SendEmailProperties sendEmailProperties = new SendEmailProperties();

}
