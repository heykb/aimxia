package com.zhu.rimxia.biz.controller;

import com.zhu.rimxia.biz.common.JsonData;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.model.domain.Member;
import com.zhu.rimxia.biz.model.modelDo.ValicodeImg;
import com.zhu.rimxia.biz.model.modelVo.MemberVo;
import com.zhu.rimxia.biz.service.MemberService;
import com.zhu.rimxia.biz.service.serviceImp.MemberServiciImpl;
import com.zhu.rimxia.biz.util.RedisUtil;
import com.zhu.rimxia.biz.util.VerifyCode;
import com.zhu.rimxia.biz.validGroup.AddGroup;
import com.zhu.rimxia.biz.validGroup.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Api(description = "登录注册相关")
@RequestMapping("/Member")
@RestController
public class MemberController {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private MemberService memberService;
    @Value("${address:#{'127.0.0.1'}}")
    private String serverAddress;
    @Value("${port}")
    private String port;

    /**
     * 注册用户
     * @param memberVo
     * @return
     */
    /**
     * 邮箱注册用户，需验证邮箱验证码
     * @param memberVo
     * @return
     */
    @ApiOperation(value = "邮箱注册用户")
    @PostMapping("/register")
    public JsonData<Member> register(@RequestBody @Validated(AddGroup.class) MemberVo memberVo){
        return JsonData.ok(memberService.register(memberVo));
    }

    /**
     * 发送邮箱验证码
     * @param emailAddr
     * @return
     */
    @ApiOperation("发送邮箱验证码")
    @ApiImplicitParam(name = "emailAddr", value = "邮箱地址", required = true, paramType = "path", dataType = "String")
    @GetMapping("/sendEmailValicode/{emailAddr}")
     public JsonData sendEmailValicode(@PathVariable String emailAddr){
        memberService.sendEmailValicode(emailAddr);
        return JsonData.ok();
     }

    /**
     * 返回验证码包括key和图片url
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ApiOperation("获取验证码包括key和图片url")
    @GetMapping(value = "/makeValicoeImg")
    public JsonData<ValicodeImg> getValicode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String valicodeKey = UUID.randomUUID().toString();

        //1分钟过期
        //redisUtil.set(valicodeKey,VerifyCode.getText(),60*1000);
        ValicodeImg valicodeImg = ValicodeImg.builder()
                .key(valicodeKey)
                .imgUrl("http://"+serverAddress+":"+port+"/Member/getValicodeimg/"+valicodeKey)
                .build();
        return JsonData.ok(valicodeImg);
    }

    /**
     * 通过key获取验证码图片
     * @param key
     * @param response
     * @throws IOException
     */
    @ApiOperation("通过key获取验证码图片")
    @ApiImplicitParam(name="key",value = "验证码key",paramType = "path",required = true,dataType = "String")
    @GetMapping("/getValicodeimg/{key}")
    public void img(@PathVariable("key")String key,HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        redisUtil.set(key,VerifyCode.getText(), MemberServiciImpl.IMG_VALICODE_TIMEOUT);
        String valicode = (String)redisUtil.get(key);
        if(valicode == null){
            throw new BusinessException(CommonErrorCode.CURRENCY,"此验证码key失效");
        }
        ImageIO.write(VerifyCode.getImage(valicode),"JPEG",response.getOutputStream());
    }

    /**
     * 邮箱激活账号，发送激活链接到邮箱，时限2天
     * @param loginName
     * @return
     */
    @ApiOperation("邮箱激活账号，发送激活链接到邮箱，时限2天")
    @ApiImplicitParam(name="loginName",value = "用户Id",paramType = "path",required = true,dataType = "string")
    @GetMapping("/sendActivationUrl/{loginName}")
    public JsonData sendActivationUrl(@PathVariable("loginName") String  loginName) {
        System.out.println(loginName);
        memberService.sendActivationUrl(loginName);
        return JsonData.ok();
    }

    @ApiOperation("激活链接验证，激活账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",paramType = "path",required = true,dataType = "long"),
            @ApiImplicitParam(name="activationCode",value = "激活码",paramType = "path",required = true,dataType = "String")
    })
    @GetMapping("/activation/{userId}/{activationCode}")
    public JsonData activation(@PathVariable("userId") Long userId,
                               @PathVariable("activationCode") String activationCode){
        memberService.activation(userId,activationCode);
        return JsonData.ok();
    }

    /**
     * 获取用户邮箱
     * @param userId
     * @return
     */
    @ApiOperation("获取用户邮箱")
    @ApiImplicitParam(name="userId",value = "用户Id",paramType = "path",required = true,dataType = "long")
    @GetMapping("/getEmailAddr/{userId}")
    public JsonData<String> getEmailAddr(@PathVariable Long userId){
        return JsonData.ok(memberService.getEmailAddr(userId));
    }

    @ApiOperation("修改用户头像")
    @PutMapping("/changeImg")
    public JsonData changeHeadImg(@RequestBody @Validated(UpdateGroup.class) MemberVo memberVo){
        memberService.changeHeadImg(memberVo);
        return JsonData.ok();
    }

    @ApiOperation("通过邮箱修改用户密码")
    @PutMapping("/changePassword")
    public JsonData changePassword(@RequestBody @Validated(UpdateGroup.class) MemberVo memberVo){
        return JsonData.ok();
    }


    @ApiOperation("获取用户头像链接")
    @ApiImplicitParam(name="userId",value = "用户Id",paramType = "path",required = true,dataType = "long")
    @GetMapping("/getHeadImg/{userId}")
    public JsonData<String> getHeadImg(@PathVariable("userId") Long userId){
        return JsonData.ok(memberService.getHeadImg(userId));
    }

}
