package com.zhu.rimxia.biz.service;

import com.zhu.rimxia.biz.model.domain.Comment;
import com.zhu.rimxia.biz.model.modelVo.CommentVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CommentService {
    Comment add(CommentVo commentVo);
    Comment getById(Long commentId);
    List<Comment> getComments(Long targetId, Integer pageNum,Integer pageSize);
    void likeComment(Long userId,Long commentId);
}
