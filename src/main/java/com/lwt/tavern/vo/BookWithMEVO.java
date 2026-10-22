package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithMEVO {

    private BookVO bookVO;

    private Integer isLike;
    private Integer isStar;
}
