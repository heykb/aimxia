package com.zhu.rimxia.biz.controller;

import com.github.pagehelper.Page;
import com.zhu.rimxia.biz.common.JsonData;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.exception.MemberException;
import com.zhu.rimxia.biz.jwt.RedisTokenStore;
import com.zhu.rimxia.biz.jwt.Token;
import com.zhu.rimxia.biz.model.domain.Member;
import com.zhu.rimxia.biz.model.domain.VideoInfo;
import com.zhu.rimxia.biz.model.modelDo.VideoInfoDo;
import com.zhu.rimxia.biz.model.modelVo.LoginVo;
import com.zhu.rimxia.biz.service.MemberService;
import com.zhu.rimxia.biz.util.PasswordUtil;
import com.zhu.rimxia.biz.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class LoginController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MemberService memberService;
    @Resource
    private VideoInfoController videoInfoController;;

    @Resource
    private RedisTokenStore redisTokenStore;


    @GetMapping("/login")
    public String  login(){
        return "login";
    }


    @PostMapping("/videosTemplate")
    public String  videosTemplate(@RequestBody List<VideoInfo> videos, Model model){

        model.addAttribute("results",videos);
        return "/hello :: bodys";
    }
    @PostMapping("/videoInfoTemplate")
    public String  videoInfoTemplate(@RequestBody VideoInfoDo videoInfoDo, Model model){

        model.addAttribute("videoInfoDo",videoInfoDo);
        return "/videoInfo :: container";
    }


    @PostMapping("/login")
    @ResponseBody
    public JsonData<String> hello(@RequestBody @Validated LoginVo loginVo){

        //System.out.println(username+": "+password);
        String valicodeDb = (String)redisUtil.get(loginVo.getKey());
        if(valicodeDb==null){
            throw new BusinessException(CommonErrorCode.VALICODE_EXPIRED);
        }
        //System.out.println(valicodeDb+"===="+loginVo.getValicode()+"===="+loginVo.getLoginName());

        if(!valicodeDb.toLowerCase().equals(loginVo.getValicode().toLowerCase())){
            throw new BusinessException(CommonErrorCode.VALICODE_ERROR);
        }
        String loginName = loginVo.getLoginName();
        List<Member> members = memberService.selectColum("userName",loginName);
        if(members.isEmpty()){
            members = memberService.selectColum("emailAddr",loginName);
            if(members.isEmpty()){
                throw new BusinessException(CommonErrorCode.ACCOUNT_NOT_EXISTS);
            }
        }
        Member member = members.get(0);
        String passwordDb = member.getPassword();
        String salt = member.getSalt();
        if(!PasswordUtil.genPassword(loginVo.getPassword(),passwordDb,salt)){
            throw new BusinessException(CommonErrorCode.PASSWORD_ERROR);
        }

        //被禁用
        if(member.getIsDisable()!=0){
            if(member.getIsDisable()==1){
                throw new BusinessException(MemberException.USER_IS_DISABLED_1);
            }else  if(member.getIsDisable()==2){
                throw new BusinessException(MemberException.USER_IS_DISABLED_2);
            }else  if(member.getIsDisable()==3){
                throw new BusinessException(MemberException.USER_IS_DISABLED_3);
            }else {
                throw new BusinessException(MemberException.USER_IS_DISABLED_4);
            }
        }

        //生成token
        String[] permissions = member.getPermissions()==null? null:member.getPermissions().split(",");
        String[] roles =  member.getRoles()==null? null: member.getRoles().split(",");
        Token token = redisTokenStore.registerToken(member.getUserId().toString(),permissions,roles);
        return JsonData.ok(token.getAccessToken());
    }


}
