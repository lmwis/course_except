package com.fehead.course.properties;

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
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private SmsProperties smsProperties = new SmsProperties();

    private SendEmailProperties sendEmailProperties = new SendEmailProperties();

    public SmsProperties getSmsProperties() {
        return smsProperties;
    }

    public void setSmsProperties(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public SendEmailProperties getSendEmailProperties() {
        return sendEmailProperties;
    }

    public void setSendEmailProperties(SendEmailProperties sendEmailProperties) {
        this.sendEmailProperties = sendEmailProperties;
    }
}
