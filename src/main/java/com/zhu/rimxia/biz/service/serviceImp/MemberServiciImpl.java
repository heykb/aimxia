package com.zhu.rimxia.biz.service.serviceImp;

import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.exception.MemberException;
import com.zhu.rimxia.biz.exception.SystemException;
import com.zhu.rimxia.biz.mapper.ActivationCodeMapper;
import com.zhu.rimxia.biz.mapper.MemberMapper;
import com.zhu.rimxia.biz.model.domain.ActivationCode;
import com.zhu.rimxia.biz.model.domain.Member;
import com.zhu.rimxia.biz.model.modelVo.MemberVo;
import com.zhu.rimxia.biz.service.MemberService;
import com.zhu.rimxia.biz.util.*;
import com.zhu.rimxia.biz.util.id.IdUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MemberServiciImpl implements MemberService {
    //验证码10分钟过期
    public final static Long Email_VALICODE_TIMEOUT = 60*10L;
    //激活链接2天过期
    public final static Long ACTIVATIOLINK_TIMEOUT = 60*60*48L;
    //图片验证码1分钟过期
    public final static Long IMG_VALICODE_TIMEOUT = 60L;


    @Resource
    private MemberMapper memberMapper;
    @Resource
    private ValicodeUtil valicodeUtil;
    @Resource
    private ActivationCodeMapper activationCodeMapper;
    @Resource
    private EmailUtil emailUtil;

    @Value("${address:#{'127.0.0.1'}}")
    private String serverAddress;
    @Value("${port}")
    private String port;

    @Override
    public Member register(MemberVo memberVo) {
        Member member = Member.builder()
                .build();
        BeanUtil.copyPropertiesIgnoreNull(memberVo,member);
        member.setUserId(IdUtil.generateId());
        member.setIsDel(false);
        //一级禁用，发送邮箱链接直接激活
        member.setIsDisable(1);
        //用户名为空默认为id
        if(member.getUserName()==null){
            member.setUserName(UUID.randomUUID().toString());
        }


        //验证注册会员信息是否重复，用户名、邮箱
        valifyMember(memberVo);

        //验证码验证
        if(!valicodeUtil.valify(memberVo.getValiCode(),memberVo.getEmailAddr())){
            throw new BusinessException(MemberException.VALICODE_NO);
        }
        //密码加密存储
        PasswordObject passwordObject = PasswordUtil.password(memberVo.getPassword());
        member.setPassword(passwordObject.getPassword());
        member.setSalt(passwordObject.getSalt());
        memberMapper.insertSelective(member);

        return member;
    }

    /**
     * 发送邮箱验证码
     * @param emailAddr
     */
    @Override
    public void sendEmailValicode(String emailAddr) {
        valicodeUtil.sendValicodeToEmial(emailAddr,Email_VALICODE_TIMEOUT);
    }

    /**
     * 验证注册信息是否存在
     * @return
     */
    private void valifyMember(MemberVo memberVo){
        List<Member> members = selectColum("emailAddr",memberVo.getEmailAddr());
        List<Member> members1 = null;
        if(memberVo.getUserName()!=null){
            members1 = selectColum("userName",memberVo.getUserName());
            if(!members1.isEmpty()){
                throw  new BusinessException(MemberException.LOGINNAME_IS_EXIST);
            }

        }
        if(!members.isEmpty()) throw  new BusinessException(MemberException.EAMIL_IS_EXIST);
    }

    /**
     * 通过某字段查询用户
     * @param colum
     * @param value
     * @return
     */
    @Override
    public List<Member> selectColum(String colum, Object value){
        Example example = new Example(Member.class);
        example.createCriteria()
                .andEqualTo(colum,value)
                .andEqualTo("isDel",false);
        return memberMapper.selectByExample(example);
    }


    /**
     * 发送激活链接
     * @param loginName
     * @throws MessagingException
     */
    @Override
    public void sendActivationUrl(String loginName) {
        if(loginName == null ){
            throw new BusinessException(CommonErrorCode.ILLEGAL_PARAM_EMPTY,"loginName");
        }
        List<Member> members = selectColum("emailAddr",loginName);
        if(members.isEmpty()){
            members = selectColum("userName",loginName);
        }
        Member member = members.get(0);
         //账户已经激活
        if(member.getIsDisable()==0){
            throw new BusinessException(CommonErrorCode.MEMBER_IS_ACITON_STATUS,loginName);
        }

        ActivationCode activationCodeOb = activationCodeMapper.selectByPrimaryKey(member.getUserId());
        //若已发送过激活链接
        if(activationCodeOb != null){
           long aliveTime = System.currentTimeMillis()-activationCodeOb.getMadeTime();
            if(aliveTime<0){
                throw new SystemException(CommonErrorCode.INNER_ERROR,"系统时间回滚或数据库数据异常");
            }else if(aliveTime>ACTIVATIOLINK_TIMEOUT){//过期，刷新激活码
                activationCodeOb.setActivationCode(UUID.randomUUID().toString());
                activationCodeOb.setMadeTime(System.currentTimeMillis());
            }else{//未过期，刷新过期时间
                activationCodeOb.setMadeTime(System.currentTimeMillis());
            }
            activationCodeMapper.updateByPrimaryKey(activationCodeOb);
        }
        //未发送过激活链接
        if(activationCodeOb == null){
            String activationCode = UUID.randomUUID().toString();
            Long madeTime = System.currentTimeMillis();
            activationCodeOb = ActivationCode.builder()
                    .userId(member.getUserId())
                    .activationCode(activationCode)
                    .madeTime(madeTime)
                    .build();
            activationCodeMapper.insert(activationCodeOb);
        }


        String link = "http://"+serverAddress+":"+port+
                "/Member/activation/"+member.getUserId()+
                "/"+activationCodeOb.getActivationCode();
        String emailAddr = member.getEmailAddr();
        Map<String,String>  parms = new HashMap<>();
        parms.put("link",link);
        emailUtil.sendtoEmail(emailAddr,parms,EmailUtil.ACTIVATION_TEMPLATE);
    }

    /**
     * 通过id获取用户
     * @param userId
     * @return
     */
    @Override
    public Member getById(Long userId) {
        List<Member> members = selectColum("userId",userId);
        if(members.isEmpty()){
            throw new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"member");
        }
        return members.get(0);
    }

    /**
     * 获取用户邮箱地址
     * @param userId
     * @return
     */
    @Override
    public String getEmailAddr(Long userId) {
        List<Member> members = selectColum("userId",userId);
        return members.get(0).getEmailAddr();
    }

    /**
     * 验证激活链接，激活账户
     * @param userId
     * @param activationCode
     */
    @Override
    @Transactional
    public void activation(Long userId, String activationCode) {
        ActivationCode activationCodeOb = activationCodeMapper.selectByPrimaryKey(userId);
        if(activationCodeOb == null){
            throw new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"ActivationCode");
        }
        String activationCodDb = activationCodeOb.getActivationCode();

        long aliveTime = System.currentTimeMillis()-activationCodeOb.getMadeTime();
        if(aliveTime<0){
            throw new SystemException(CommonErrorCode.INNER_ERROR,"系统时间回滚或数据库数据异常");
        }else if(aliveTime>ACTIVATIOLINK_TIMEOUT){
            throw new BusinessException(CommonErrorCode.EMAIL_LINK_EXPIRE);
        }
        if(!activationCodDb.equals(activationCode)){
            throw new BusinessException(CommonErrorCode.EAMIL_ACTIVATION_CODE_ERROR);
        }
        activationCodeMapper.deleteByPrimaryKey(userId);
        Member member = Member.builder()
                .userId(userId)
                .isDisable(0)
                .build();
        memberMapper.updateByPrimaryKeySelective(member);
    }

    @Override
    public void changeHeadImg(MemberVo memberVo) {
        if(memberVo.getHeadImg()==null){
            throw new BusinessException(CommonErrorCode.ILLEGAL_PARAM_EMPTY,"headImg");
        }
        getById(memberVo.getUserId());
        Member member = Member.builder()
                .userId(memberVo.getUserId())
                .headImg(memberVo.getHeadImg())
                .build();
        memberMapper.updateByPrimaryKeySelective(member);
    }

    @Override
    public void changePassword(MemberVo memberVo) {
        if(memberVo.getPassword()==null){
            throw new BusinessException(CommonErrorCode.ILLEGAL_PARAM_EMPTY,"password");
        }
        if(memberVo.getValiCode()==null){
            throw new BusinessException(CommonErrorCode.ILLEGAL_PARAM_EMPTY,"valicode");
        }

        //valicodeUtil.valify()

        List<Member> members = selectColum("emailAddr",memberVo.getEmailAddr());
        String password = memberVo.getPassword();
        if(PasswordUtil.genPassword(password,members.get(0).getPassword(),members.get(0).getSalt())){
            throw new BusinessException(CommonErrorCode.NEW_PASSWORD_SAME_AS_LAST);
        };


    }

    /**
     * 查询用户头像
     */
    @Override
    public String getHeadImg(Long userId) {

        Member member = getById(userId);

        return member.getHeadImg();
    }


}
