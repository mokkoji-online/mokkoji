package online.mokkoji.common.auth.oauth2.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.common.auth.oauth2.dto.response.UserInfoResDto;
import online.mokkoji.common.auth.oauth2.service.OAuth2Service;
import online.mokkoji.common.auth.oauth2.service.OAuth2ServiceImpl;
import online.mokkoji.user.domain.Provider;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/oauth2")
public class OAuth2Controller {

    private final OAuth2ServiceImpl oAuth2Service;

    @GetMapping("/{provider}/{code}")
    public ResponseEntity<UserInfoResDto> getUserInfo(@PathVariable String provider, @PathVariable String code,
                                                      HttpServletResponse res) throws Exception {

        Map<String, Object> result = oAuth2Service.getNaverUserInfo(code);

        UserInfoResDto userInfoResDto = (UserInfoResDto) result.get("userInfo");

        String accessToken = (String) result.get("accessToken");
        res.addHeader("Authorization", accessToken);

        if(result.containsKey("refreshToken")) {
            String refreshToken = (String) result.get("refreshToken");
            res.addHeader("Authorization-Refresh", refreshToken);
        }

        return new ResponseEntity<>(userInfoResDto, HttpStatus.OK);
    }
}
