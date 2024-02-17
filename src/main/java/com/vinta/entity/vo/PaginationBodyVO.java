package com.vinta.entity.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PaginationBodyVO {
    private Integer pageSize=10;
    private Integer pageNum=0;
    private Integer total;
    private String category;
    private String searchKey;
    private String cursorScore;
    private String userId;
}
