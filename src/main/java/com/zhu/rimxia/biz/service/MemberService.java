package com.zhu.rimxia.biz.service;

import com.zhu.rimxia.biz.model.domain.Member;
import com.zhu.rimxia.biz.model.modelVo.MemberVo;

import java.util.List;


public interface MemberService {
    Member register(MemberVo memberVo);


    void sendEmailValicode(String emailAddr);

    List<Member> selectColum(String colum, Object value);

    void sendActivationUrl(String loginName);

    Member getById(Long userId);

    String getEmailAddr(Long userId);

    void activation(Long userId,String activationCode);

    void changeHeadImg(MemberVo memberVo);

    void changePassword(MemberVo memberVo);

    String getHeadImg(Long userId);

}
