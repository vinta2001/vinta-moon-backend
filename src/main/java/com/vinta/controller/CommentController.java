package com.vinta.controller;

import com.vinta.annotation.UserLoginRequired;
import com.vinta.entity.dto.CommentPageDTO;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.entity.po.CommentInfo;
import com.vinta.entity.vo.CommentBodyVO;
import com.vinta.entity.vo.CommentPageVO;
import com.vinta.service.CommentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/comment")
@Tag(name = "评论相关接口")
public class CommentController {
    @Resource
    private CommentInfoService commentInfoService;

    //添加评论
    @PostMapping("/add")
    @Operation(summary = "添加评论")
//    @UserLoginRequired
    public ResultDTO addComment(@RequestBody CommentBodyVO commentBody){
        commentInfoService.addComment(commentBody);
        return ResultDTO.ok();
    }

    //获取评论
    @GetMapping("/get")
    @Operation(summary = "获取评论")
    public ResultDTO<CommentPageDTO> getComment(CommentPageVO commentPageVO){
        CommentPageDTO comments = commentInfoService.getComments(commentPageVO);
        return ResultDTO.ok(comments);
    }

    //删除评论
    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "删除评论")
    public ResultDTO deleteComment(@PathVariable("commentId") Integer commentId){
        commentInfoService.deleteComment(commentId);
        return ResultDTO.ok();
    }
}
