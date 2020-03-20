package com.zhu.rimxia.biz.config;

import com.sun.deploy.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;

@Aspect
@Configuration
@Slf4j
public class LogAop {

    @Pointcut("execution(public * com.zhu.rimxia.biz.controller..*.*(..))")
    public void pointCut(){};

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        printMethodParams(joinPoint);
    }
    private void printMethodParams(JoinPoint joinPoint){
        /**
         * Signature 包含了方法名、申明类型以及地址等信息
         */
        String class_name = joinPoint.getTarget().getClass().getName();
        String method_name = joinPoint.getSignature().getName();
        log.warn("接口 = {}",class_name);
        log.warn("方法名 = {}", method_name);

        /**
         * 获取方法的参数值列表
         */
        Object[] args_value = joinPoint.getArgs();
        /**
         * 获取参数名列表
         */
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        try {
            String[] args_name = getParamName(method);
            logParam(args_name, args_value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取参数名列表
     * @param method
     * @return
     * @throws ClassNotFoundException
     */
    private String[] getParamName(Method method) throws ClassNotFoundException {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        return u.getParameterNames(method);
    }
    /**
     * 日志打印参数列表
     * @param args_name
     * @param args_value
     */
    private void logParam(String[] args_name, Object[] args_value) {
        if(ArrayUtils.isEmpty(args_name) || ArrayUtils.isEmpty(args_value)){
            log.warn("该方法没有参数");
            return ;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0; i<args_name.length;i++){
            String name = args_name[i];
            Object value = args_value[i]==null?"null":args_value[i];
            stringBuffer.append(name+" = ");
            if(isPrimite(value.getClass())){
                stringBuffer.append(value);
            }else stringBuffer.append(value.toString());

            if(i < args_name.length-1){
                stringBuffer.append(" , ");
            }
        }
        log.warn(stringBuffer.toString());
    }

    private boolean isPrimite(Class<?> clazz){
        if(clazz.isPrimitive() || clazz==String.class){
            return true;
        }else return false;
    }

}
