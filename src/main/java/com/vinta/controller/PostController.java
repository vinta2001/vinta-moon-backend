package com.vinta.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vinta.entity.dto.PaginationResultDTO;
import com.vinta.entity.po.PostInfo;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.vo.PaginationBodyVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.entity.vo.PostResultVO;
import com.vinta.entity.vo.UserResultVO;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.service.MediaInfoService;
import com.vinta.service.PostInfoService;
import com.vinta.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "获取笔记的内容和评论")
@RequestMapping("/post")
public class PostController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private MediaInfoService mediaInfoService;

    @Resource
    private PostInfoService postInfoService;

    private PaginationResultDTO<PostResultVO> getResultVOPaginationResultDTO(PaginationBodyVO paginationBodyVO, IPage<PostInfo> postInfoIPage, List<PostResultVO> postResultVOS) {
        PaginationResultDTO<PostResultVO> postResultVOPaginationResultDTO = new PaginationResultDTO<>();
        postResultVOPaginationResultDTO.setCursorScore(paginationBodyVO.getCursorScore());
        postResultVOPaginationResultDTO.setTotal(postInfoIPage.getTotal());
        postResultVOPaginationResultDTO.setCategory(paginationBodyVO.getCategory());
        postResultVOPaginationResultDTO.setSearchKey(paginationBodyVO.getSearchKey());
        postResultVOPaginationResultDTO.setPageNum(postInfoIPage.getPages());
        postResultVOPaginationResultDTO.setPageSize(postInfoIPage.getSize());
        postResultVOPaginationResultDTO.setDatas(postResultVOS);
        return postResultVOPaginationResultDTO;
    }

    private PostResultVO convert2PostResultVO(PostInfo postInfo) {
        if (postInfo == null) {
            throw new BusinessException("数据不存在");
        }
        PostResultVO postResultVO = new PostResultVO();
        String userId = postInfo.getUserId();
        UserInfo userInfo = userInfoService.getUserByUserId(userId);
        UserResultVO user = new UserResultVO(userInfo);
        postResultVO.setUser(user);
        String postId = postInfo.getPostId();
        List<String> mediaUrl = mediaInfoService.getMediaByPostId(postId);
        postResultVO.setId(postId);
        postResultVO.setDesc(postInfo.getDescription());
        postResultVO.setContent(postInfo.getTextContent());
        postResultVO.setComments(postInfo.getCommentsNum());
        postResultVO.setCollects(postInfo.getCommentsNum());
        postResultVO.setThumbs(postInfo.getThumbNum());
        postResultVO.setMusicLink(postInfo.getMusicLink());
        // todo 需要对数据表中的like进行设置，默认为false
        postResultVO.setLike(false);
        postResultVO.setCover(mediaUrl.get(0));
        return postResultVO;
    }

    @PostMapping("/note/feed")
    @Operation(summary = "获取笔记")
    public ResultDTO getPost(@RequestBody PaginationBodyVO paginationBodyVO) {

        IPage<PostInfo> postInfoIPage = postInfoService.findPostListByQuery(paginationBodyVO);
        List<PostInfo> records = postInfoIPage.getRecords();
        List<PostResultVO> postResultVOS = new ArrayList<>();
        for (PostInfo postInfo : records) {
            PostResultVO postResult = convert2PostResultVO(postInfo);
            postResultVOS.add(postResult);
        }
        PaginationResultDTO<PostResultVO> postResultVOPaginationResultDTO = getResultVOPaginationResultDTO(paginationBodyVO, postInfoIPage, postResultVOS);
        return ResultDTO.ok(postResultVOPaginationResultDTO);
    }

    @PostMapping("/note/upload")
    @Operation(summary = "上传笔记")
    public ResultDTO uploadPost(@RequestBody PostBodyVO postBodyVO) {
        int res = postInfoService.insertOne(postBodyVO);
        // todo 查询文件
        return res==1?ResultDTO.success(StatusCode.UPDATE_SUCCESS):ResultDTO.failed(StatusCode.UPLOAD_ERROR);
    }

    @GetMapping("/comment/feed")
    @Operation(summary = "获取评论",
            parameters = {
                    @Parameter(name = "num", description = "获取的数量", required = true),
                    @Parameter(name = "note_id", description = "笔记id", required = true),
                    @Parameter(name = "root_comment_id", description = "根评论id", required = true)
            })
    public ResultDTO getComment(@RequestParam("note_id") String postId,
                                @RequestParam("root_comment_id") String rootCommentId,
                                @RequestParam("num") Integer num) {
        return ResultDTO.ok("ok");
    }

    @GetMapping("/comment/upload")
    @Operation(summary = "上传评论",
            parameters = {
                    @Parameter(name = "user_id", description = "用户id", required = true),
                    @Parameter(name = "root_comment_id", description = "根评论id", required = true),
                    @Parameter(name = "content", description = "评论内容", required = true)
            })
    public ResultDTO uploadComment(@NotBlank @RequestHeader("Authorization") String Authorization,
                                   @RequestParam("root_comment_id") String rootCommentId,
                                   @RequestParam("content") String content) {
        return ResultDTO.ok("ok");
    }
}
