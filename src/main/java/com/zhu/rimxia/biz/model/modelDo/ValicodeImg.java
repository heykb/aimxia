package com.zhu.rimxia.biz.model.modelDo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValicodeImg {
    private String key;
    private String imgUrl;
}
