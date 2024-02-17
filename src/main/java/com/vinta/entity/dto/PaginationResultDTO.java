package com.vinta.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaginationResultDTO<T> {
    private Long pageSize;
    private Long pageNum;
    private Long total;
    private String category;
    private String searchKey;
    private String cursorScore;
    private List<T> datas = new ArrayList <T>();
}
