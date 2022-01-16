package com.ecommerce.newshop1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private int expires_in;
    private int refresh_token_expires_in;

}
