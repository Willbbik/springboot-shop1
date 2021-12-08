package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.KakaoDto;
import com.ecommerce.newshop1.dto.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private static final Logger log = LoggerFactory.getLogger(KakaoService.class);

    String Content_type = "application/x-www-form-urlencoded;charset=utf-8";

    @Value("${kakao.grant_type}")
    String grant_type;

    @Value("${kakao.client_id}")
    String client_id;

    @Value("${kakao.redirect_uri}")
    String redirect_uri;

    @Value("${kakao.logout_redirect_uri")
    String logout_redirect_uri;

    @Value("${kakao.provider.oauth_token")
    String oauthTokenUrl;

    @Value("${kakao.provider.user_info")
    String userInfoUrl;

    public OAuthToken getAccessToken(String code){

        RestTemplate rt = new RestTemplate();

        // http headers
        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", Content_type);

        // http body에 key-value 형태로 값을 담기 위해서
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grant_type);
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);

        // HttpHeaders와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, header);

        ResponseEntity<String> response = rt.exchange(
                oauthTokenUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = new OAuthToken();

        try{
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            log.info("KakaoService JsonMappingException 66 line, reason : " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info("KakaoService JsonProcessingException 66 line, reason : " + e.getMessage());
            e.printStackTrace();
        }
        return oAuthToken;
    }

    // AccessToken으로 사용자 정보 가져오기
    public KakaoDto getUserKakaoProfile(String access_token){

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // 카카오에서 해당 Content-type을 요구함
        headers.add("Authorization", "Bearer "+access_token);
        headers.add("Content-type", Content_type);

        // HttpHeaders만 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // Http 요청하기
        ResponseEntity<String> response = rt.exchange(
                userInfoUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        ObjectMapper objMapper = new ObjectMapper();
        KakaoDto kakaoDto = new KakaoDto();
        try {
            kakaoDto = objMapper.readValue(response.getBody(), KakaoDto.class);
        } catch (JsonMappingException e) {
            log.info("KakaoService JsonMappingException 103 line, reasone : " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info("KakaoService JsonProcessingException 103 line, reasone : " + e.getMessage());
            e.printStackTrace();
        }

        return kakaoDto;
    }


}
