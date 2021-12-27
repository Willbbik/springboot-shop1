package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.KakaoDto;
import com.ecommerce.newshop1.dto.KakaoPayDto;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Value("${kakao.provider.oauth_token}")
    String oauthTokenUrl;

    @Value("${kakao.provider.user_info}")
    String userInfoUrl;

    @Value("${kakao.admin_key}")
    String adminKey;

    @Value("${kakaoPay.ready_uri}")
    String kakaoPayReadyUri;

    @Value("${kakaoPay.approve_uri}")
    String kakaoPayApproveUri;

    // 카카오페이 결제 준비
    public String kakaoPayReady(HttpServletRequest request){

        HttpSession session = request.getSession();

        RestTemplate rt = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", Content_type);
        header.add("Authorization", "KakaoAK "+adminKey);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "partner_order_id");
        params.add("partner_user_id", "partner_user_id");
        params.add("item_name", "초코파이");
        params.add("quantity", "1");
        params.add("total_amount", "2000");
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8080/order/kakaoPay/success");
        params.add("fail_url", "http://localhost:8080/order/kakaoPay/fail");
        params.add("cancel_url", "http://localhost:8080/order/kakaoPay/cancel");

        HttpEntity<MultiValueMap<String, String>> kakaoPayRequest = new HttpEntity<>(params, header);
        ResponseEntity<String> response = rt.exchange(
                kakaoPayReadyUri,
                HttpMethod.POST,
                kakaoPayRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            KakaoPayDto kakaoPayDto = objectMapper.readValue(response.getBody(), KakaoPayDto.class);
            session.setAttribute("tid", kakaoPayDto.getTid());
            // 주문번호도 세션에 저장해야함

            return kakaoPayDto.getNext_redirect_pc_url();
        } catch (JsonMappingException e) {
            log.info("KakaoService kakaoPayReady method error, reason  : " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info("KakaoService kakaoPayReady method error, reason : " + e.getMessage());
            e.printStackTrace();
        }
        return "fail";
    }

    public void kakaoPayApprove(String pgToken, String tid){

        RestTemplate rt = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-type", Content_type);
        header.add("Authorization", "KakaoAK "+adminKey);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("pg_token", pgToken);
        params.add("partner_order_id", "partner_order_id");
        params.add("partner_user_id", "partner_user_id");

        HttpEntity<MultiValueMap<String, String>> kakaoPayRequest = new HttpEntity<>(params, header);

        ResponseEntity<String> response = rt.exchange(
                kakaoPayApproveUri,
                HttpMethod.POST,
                kakaoPayRequest,
                String.class
        );
    }


    // 액세스 토큰 가져오기
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
            log.info("KakaoService getAccessToken method error, reason  : " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info("KakaoService getAccessToken method error, reason : " + e.getMessage());
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
