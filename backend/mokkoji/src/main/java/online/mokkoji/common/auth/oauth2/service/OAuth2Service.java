package online.mokkoji.common.auth.oauth2.service;

import online.mokkoji.common.auth.oauth2.dto.response.UserInfoResDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface OAuth2Service {


    HttpEntity<MultiValueMap<String, String>> generateNaverTokenReq(String authorizationCode) throws Exception;

    HttpEntity<MultiValueMap<String, String>> generateNaverProfileReq(String accessToken) throws Exception;

    Map<String, Object> getNaverUserInfo(String accessToken) throws Exception;

}