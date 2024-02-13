package com.vinta.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name="操作笔记，包括文章，图片，视频，以及相关的评论")
@RequestMapping("/post")
public class PostController {
}
