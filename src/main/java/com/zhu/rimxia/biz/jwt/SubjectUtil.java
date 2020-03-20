package com.zhu.rimxia.biz.jwt;

import com.zhu.rimxia.biz.annotation.Logical;

/**
 * 检查权限工具类
 */
public class SubjectUtil {

    public static final String REQUEST_TOKEN_NAME = "TokenObject";
    public static  boolean hasRole(Token token, String[] requiredRoles, Logical logical){

        String[] rolesUser = token.getRoles();
        boolean re = false;
            for(String role:requiredRoles){
                for(String r: rolesUser){
                    if(role.equals(r)){
                        re = true;
                        break;
                    }
                }
                if(logical == (re? Logical.OR:Logical.AND)){
                    break;
                }else{
                    re = false;
                }
        }
        return re;
    }
}
