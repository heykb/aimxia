package com.zhu.rimxia.biz.service.serviceImp;

import com.github.pagehelper.PageHelper;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import com.zhu.rimxia.biz.mapper.CommentMapper;
import com.zhu.rimxia.biz.mapper.VideoMapper;
import com.zhu.rimxia.biz.model.domain.Comment;
import com.zhu.rimxia.biz.model.domain.Member;
import com.zhu.rimxia.biz.model.domain.Notify;
import com.zhu.rimxia.biz.model.domain.Video;
import com.zhu.rimxia.biz.model.modelVo.CommentVo;
import com.zhu.rimxia.biz.service.CommentService;
import com.zhu.rimxia.biz.service.MemberService;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.service.VideoService;
import com.zhu.rimxia.biz.util.id.IdUtil;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private MemberService memberService;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private VideoService videoService;

    @Resource
    private NotifyService notifyService;
    /**
     * 添加评论
     * @param commentVo
     */
    @Override
    public Comment add(CommentVo commentVo) {
        Member member =  memberService.getById(commentVo.getCreaterId());
        commentVo.setIsAuth(false);
        //如果是评论
        if("comment".equals(commentVo.getTargetType())){
            Comment target = getById(commentVo.getTargetId());
            //System.out.println(target.getCreaterId()+"-------"+commentVo.getCreaterId());
            //是否是自己评价自己的作品
           if(target.getCreaterId().equals(commentVo.getCreaterId())){
                commentVo.setIsAuth(true);
            }
        }else if("anime".equals(commentVo.getTargetType())){
            videoService.getById(commentVo.getTargetId());
        }else{
            throw  new BusinessException(CommonErrorCode.ILLEGAL_PARAM,"targetType目标类型错误");
        }



        Comment comment = Comment.builder()
                .commentId(IdUtil.generateId())
                .targetId(commentVo.getTargetId())
                .content(commentVo.getContent())
                .isAuth(commentVo.getIsAuth())
                .likeNum(0)
                .createTime(new Date())
                .targetType(commentVo.getTargetType())
                .userHeadImg(member.getHeadImg())
                .createrId(commentVo.getCreaterId())
                .build();
        commentMapper.insert(comment);
        return comment;
    }


    @Override
    public Comment getById(Long commentId){
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if(comment == null){
            throw  new BusinessException(CommonErrorCode.OBJECT_NOT_EXISTS,"CommentTarget");
        }
        return comment;
    }

    @Override
    public List<Comment> getComments(Long targetId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("create_time desc");
        Example example = new Example(Comment.class);
        example.createCriteria().andEqualTo("targetId",targetId);
        return commentMapper.selectByExample(example);
    }

    @Override
    public void likeComment(Long userId, Long commentId) {
        memberService.getById(userId);
        Comment comment =  getById(commentId);
        comment.setLikeNum(comment.getLikeNum()+1);
        List<Notify> notifies = notifyService.find(userId,commentId,NotifyService.REMIND_TYPE_LIKE);
        if(!notifies.isEmpty()){
            throw  new BusinessException(CommonErrorCode.ALREADY_LIKE);
        }
        commentMapper.updateByPrimaryKey(comment);
    }
}
