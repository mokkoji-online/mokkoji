package online.mokkoji.user.service;

import online.mokkoji.user.domain.User;
import online.mokkoji.user.dto.request.UserInputReqDto;
import online.mokkoji.user.dto.response.MyPageResDto;
import online.mokkoji.user.dto.response.UpdatePageResDto;

public interface UserService {

    //회원 정보 수정으로 이동
    UpdatePageResDto getUpdatePage(String provider, String email);

    //마이 페이지로 이동
    MyPageResDto getMypage(String provider, String email);

    //회원 가입
    void signUp(String provider, String email, UserInputReqDto userInputReqDto);

    //회원 정보 수정
    void updateUser(String provider, String email, UserInputReqDto modifyDto);

    //회원 탈퇴
    void deleteUser(String provider, String email);

    //DB에서 회원 조회
    User searchUser(String provider, String email);
}

