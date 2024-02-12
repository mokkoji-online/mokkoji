package online.mokkoji.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorCode.*;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.dto.request.UserInputReqDto;
import online.mokkoji.user.dto.response.MyPageResDto;
import online.mokkoji.user.dto.response.UpdatePageResDto;
import online.mokkoji.user.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final JwtUtil jwtService;

    @GetMapping("/userinfo")
    public ResponseEntity<UpdatePageResDto> getUserInfo(HttpServletRequest req) {
        String provider = jwtService.getProvider(req);
        String email = jwtService.getEmail(req);

        UpdatePageResDto updatePageResDto = userServiceImpl.getUpdatePage(provider, email);

        return new ResponseEntity<>(updatePageResDto, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageResDto> getMyPage(HttpServletRequest req) {
        String provider = jwtService.getProvider(req);
        String email = jwtService.getEmail(req);

        MyPageResDto myPageResDto = userServiceImpl.getMypage(provider, email);

        return new ResponseEntity<>(myPageResDto, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<UserInputReqDto> addUser(HttpServletRequest req,
                                                   @Valid @RequestBody UserInputReqDto userInputReqDto) {
        String provider = jwtService.getProvider(req);
        String email = jwtService.getEmail(req);

        userServiceImpl.createUser(provider, email, userInputReqDto);

        return new ResponseEntity<>(userInputReqDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserInputReqDto> editUser(HttpServletRequest req,
                                                    @Valid @RequestBody UserInputReqDto modifyDto) {
        String provider = jwtService.getProvider(req);
        String email = jwtService.getEmail(req);

        userServiceImpl.updateUser(provider, email, modifyDto);

        return new ResponseEntity<>(modifyDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<User> removeUser(HttpServletRequest req) {
        String provider = jwtService.getProvider(req);
        String email = jwtService.getEmail(req);

        userServiceImpl.deleteUser(provider, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
