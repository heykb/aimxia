package com.zhu.rimxia.biz.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValicodeUtil {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private EmailUtil emailUtil;
    public  String getValicode(){
        String validatorCode = RandomStringUtils.randomNumeric(6);
        return validatorCode;
    }
    public  void sendValicodeToEmial(String emailAdd,Long time) {

        String valicode = getValicode();
        redisUtil.set(emailAdd,valicode,time);
        Map<String,String> parms = new HashMap();
        parms.put("valicode",valicode);
        emailUtil.sendtoEmail(emailAdd,parms,EmailUtil.VALICODE_TEMPLATE);
    }
    public  String getValicodeTologin(Long userid, Long time){
        String valicode = getValicode();
        redisUtil.set(userid.toString(),valicode,time);
        return valicode;
    }

    public boolean valify(String valicode,String emailAddr){
        String valicodeDb = (String) redisUtil.get(emailAddr);
        if(valicodeDb == null){
            return false;
        }else{
            if(valicodeDb.equals(valicode)) {
                System.out.println(valicodeDb+"::"+valicode);
                redisUtil.del(emailAddr);
                return true;
            }else return false;
        }
    }
    //public static void get
}
