package com.vinta.entity.dto;

import com.vinta.entity.vo.PostDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDetail {
    private PostDTO post;
    private UserDTO user;
}
