package com.zhu.rimxia.biz.util;

import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class EmailUtil {

    public final static String VALICODE_TEMPLATE = "valicode";
    public final static String ACTIVATION_TEMPLATE = "activation";
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${addr:#{'1479036859@qq.com'}}")
    private String from;



    public void sendtoEmail(String to, Map<String,String> parms, String template){
        Context context = new Context();
        for(String key:parms.keySet()){
            context.setVariable(key,parms.get(key));
        }
        //context.setVariable("valicode",valicode);
        String valicodeEmail = templateEngine.process(template,context);
       // System.out.println(valicodeEmail);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String subject = null;
        switch (template){
            case "valicode":subject = "邮箱验证";break;
            case "activation":subject = "账户激活";break;
            default:throw new BusinessException(CommonErrorCode.EMAIL_TEMPLATE,template);
        }
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(valicodeEmail,true);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            //throw new BusinessException(CommonErrorCode.CURRENCY,"邮件发送异常");
        }


        mailSender.send(mimeMessage);


    }
}
