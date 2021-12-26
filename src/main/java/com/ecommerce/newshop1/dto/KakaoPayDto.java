package com.ecommerce.newshop1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayDto {

    private String tid;
    private String next_redirect_pc_url;

}
