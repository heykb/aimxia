package com.zhu.rimxia.biz.controller;

import com.zhu.rimxia.biz.common.JsonData;
import com.zhu.rimxia.biz.model.domain.Comment;
import com.zhu.rimxia.biz.model.domain.Notify;
import com.zhu.rimxia.biz.model.domain.UserNotify;
import com.zhu.rimxia.biz.model.modelVo.CommentVo;
import com.zhu.rimxia.biz.service.CommentService;
import com.zhu.rimxia.biz.service.NotifyService;
import com.zhu.rimxia.biz.validGroup.AddGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "评论相关")
@RestController
@RequestMapping("/Comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private NotifyService notifyService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;


    @ApiOperation("添加评论")
    @PostMapping("/add")
    public JsonData<Comment> add(@RequestBody @Validated(AddGroup.class) CommentVo commentVo){
        Comment comment = commentService.add(commentVo);
        Notify notify = null;
        if(!"anime".equals(commentVo.getTargetType())){
            notify = notifyService.createRemind(comment.getTargetId(),comment.getTargetType(),
                    NotifyService.REMIND_TYPE_COMMENT,comment.getCreaterId(),comment.getCommentId().toString());
        }
        notifyService.createSubscription(comment.getCreaterId(),comment.getCommentId(),"comment",
                "sub");

        for(UserNotify userNotify: notifyService.pushNewRemindToUser(notify)){
            messagingTemplate.convertAndSend("/topic/"+userNotify.getUserId(),"有消息来了");
        }

        return JsonData.ok(comment);
    }


    @ApiOperation("获取某产品下的所有评论（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="targetId",value = "产品id",paramType = "path",required = true,dataType = "long"),
            @ApiImplicitParam(name="pageNum",value = "页数",paramType = "path",required = true,dataType = "int"),
            @ApiImplicitParam(name="pageSize",value = "页大小",paramType = "path",required = true,dataType = "int")
    })
    @GetMapping("/getComments/{targetId}/{pageNum}/{pageSize}")
    public JsonData<List<Comment>> getComments(@PathVariable("targetId")Long targetId,
                                               @PathVariable("pageNum") Integer pageNum,
                                               @PathVariable("pageSize")Integer pageSize){

        return JsonData.ok(commentService.getComments(targetId,pageNum,pageSize));
    }

    @ApiOperation("评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户id",paramType = "path",required = true,dataType = "long"),
            @ApiImplicitParam(name="commentId",value = "评论id",paramType = "path",required = true,dataType = "long")
    })
    @GetMapping("/likeComment/{userId}/{commentId}")
    public JsonData likeComment(@PathVariable("userId")Long userId, @PathVariable("commentId")Long commentId){
        commentService.likeComment(userId,commentId);
        Notify notify = notifyService.createRemind(commentId,"comment",NotifyService.REMIND_TYPE_LIKE,userId,null);
        for(UserNotify userNotify: notifyService.pushNewRemindToUser(notify)){
            messagingTemplate.convertAndSend("/topic/"+userNotify.getUserId(),"有消息来了");
        }
        return JsonData.ok();
    }
}
