package com.asong.cloud.service;

import com.asong.cloud.dao.CommentDAO;
import com.asong.cloud.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by asong 2020/1/12
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId,int entityType)
    {
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment)
    {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType)
    {
        return commentDAO.getCommentCount(entityId,entityType);
    }

}
