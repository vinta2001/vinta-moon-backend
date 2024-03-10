package com.vinta.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vinta.annotation.UserLoginRequired;
import com.vinta.constant.Constants;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping("/note/upload")
    @Operation(summary = "上传笔记")
    @Transactional
    public ResultDTO uploadPost(@Valid @RequestBody PostBodyVO postBodyVO) {
        int post = postInfoService.insertOne(postBodyVO);
        int media = mediaInfoService.insertAll(postBodyVO);
        return post == 1 && media!=0 ? ResultDTO.success(StatusCode.UPLOAD_SUCCESS) : ResultDTO.failed(StatusCode.UPLOAD_ERROR);
    }

    private PaginationResultDTO<PostResultVO> getResultVOPaginationResultDTO(PaginationBodyVO paginationBodyVO, IPage<PostInfo> postInfoIPage, List<PostResultVO> postResultVOS) {
        PaginationResultDTO<PostResultVO> postResultVOPaginationResultDTO = new PaginationResultDTO<>();
        postResultVOPaginationResultDTO.setCursorScore(paginationBodyVO.getCursorScore());
        postResultVOPaginationResultDTO.setTotal(postInfoIPage.getTotal());
        postResultVOPaginationResultDTO.setCategory(paginationBodyVO.getCategory());
        postResultVOPaginationResultDTO.setSearchKey(paginationBodyVO.getSearchKey());
        postResultVOPaginationResultDTO.setPageNum(postInfoIPage.getCurrent());
        postResultVOPaginationResultDTO.setPageSize(postInfoIPage.getSize());
        postResultVOPaginationResultDTO.setData(postResultVOS);
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
        for(int i = 0; i < mediaUrl.size(); i++) {
            mediaUrl.set(i, Constants.HOST + mediaUrl.get(i));
        }
        postResultVO.setId(postId);
        postResultVO.setDesc(postInfo.getDescription());
        postResultVO.setContent(postInfo.getTextContent());
        postResultVO.setComments(postInfo.getCommentsNum());
        postResultVO.setCollects(postInfo.getCollectNum());
        postResultVO.setThumbs(postInfo.getThumbNum());
        postResultVO.setMusicLink(postInfo.getMusicLink());
        postResultVO.setLocation(postInfo.getLocation());
        // todo 需要对数据表中的like进行设置，默认为false
        postResultVO.setLike(false);
        postResultVO.setCover(mediaUrl.get(0));
        postResultVO.setMediaUrl(mediaUrl);
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

    @GetMapping("/comment/feed")
    @Operation(summary = "获取评论")
    public ResultDTO getComment(@RequestParam("note_id") String postId,
                                @RequestParam("root_comment_id") String rootCommentId,
                                @RequestParam("num") Integer num) {
        return ResultDTO.ok("ok");
    }

    @GetMapping("/comment/upload")
    @Operation(summary = "上传评论")
    @UserLoginRequired
    public ResultDTO uploadComment(@NotBlank @RequestHeader("Authorization") String Authorization,
                                   @RequestParam("root_comment_id") String rootCommentId,
                                   @RequestParam("content") String content) {
        return ResultDTO.ok("ok");
    }
}
