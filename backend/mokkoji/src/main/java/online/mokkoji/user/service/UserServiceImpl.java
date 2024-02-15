package online.mokkoji.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.user.domain.*;
import online.mokkoji.user.domain.Record;
import online.mokkoji.user.dto.request.UserInputReqDto;
import online.mokkoji.user.dto.response.MyPageResDto;
import online.mokkoji.user.dto.response.UpdatePageResDto;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.UserErrorCode;
import online.mokkoji.user.domain.UserAccount;
import online.mokkoji.user.repository.AccountRepository;
import online.mokkoji.user.repository.RecordRepository;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RecordRepository recordRepository;
    private final JwtUtil jwtService;

    //회원 정보 수정으로 이동
    @Override
    public UpdatePageResDto getUpdatePage(String provider, String email) {
        Optional<User> findUser = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email);

        if (findUser.isEmpty()) {
            throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        }

        //회원 정보, 계좌 정보 전달
        User readUser = findUser.get();
        UserAccount userAccount = readUser.getUserAccount();

        return UpdatePageResDto.builder()
                .email(readUser.getEmail())
                .image(readUser.getImage())
                .name(readUser.getName())
                .bank(userAccount.getBank())
                .accountNumber(userAccount.getNumber())
                .build();
    }

    //마이 페이지로 이동
    @Override
    public MyPageResDto getMypage(String provider, String email) {
        //회원 정보, 계좌 정보(등록 여부), 활동 기록 전달
        User readUser = searchUser(provider, email);
        UserAccount userAccount = readUser.getUserAccount();
        Record record = readUser.getRecord();

        if (userAccount.getBank().equals("") || userAccount.getNumber().equals("")) {
            return MyPageResDto.builder()
                    .image(readUser.getImage())
                    .name(readUser.getName())
                    .isAccountRegistered(false)
                    .eventCount(record.getEventCount())
                    .totalTime(record.getTotalTime())
                    .totalParticipant(record.getTotalParticipant())
                    .totalMessage(record.getTotalMessage())
                    .build();
        }

        return MyPageResDto.builder()
                .image(readUser.getImage())
                .name(readUser.getName())
                .isAccountRegistered(true)
                .eventCount(record.getEventCount())
                .totalTime(record.getTotalTime())
                .totalParticipant(record.getTotalParticipant())
                .totalMessage(record.getTotalMessage())
                .build();
    }

    //회원 가입
    @Override
    public void signUp(String provider, String email, UserInputReqDto userInputReqDto) {
        User newUser = searchUser(provider, email);

        String refreshToken = jwtService.createRefreshToken();

        //입력 내용으로 GuestUser 정보 최신화, Guest >> User 전환, refreshToken 발급
        newUser.updateUser(userInputReqDto.getEmail(), userInputReqDto.getName(), userInputReqDto.getImage());
        newUser.updateRefreshToken(refreshToken);
        newUser.updateAuthority();
        userRepository.save(newUser);

        Record record = Record.builder()
                .user(newUser)
                .build();
        recordRepository.save(record);

        String bank = userInputReqDto.getBank();
        String accountNumber = userInputReqDto.getAccountNumber();

        UserAccount userAccount = UserAccount.builder()
                .user(newUser)
                .bank(bank)
                .number(accountNumber)
                .build();

        accountRepository.save(userAccount);
    }

    //회원 정보 수정
    @Override
    public void updateUser(String provider, String email, UserInputReqDto modifyDto) {
        User updateUser = searchUser(provider, email);

        String name = modifyDto.getName();
        String image = modifyDto.getImage();
        String bank = modifyDto.getBank();
        String accountNumber = modifyDto.getAccountNumber();

        updateUser.updateUser(email, name, image);
        userRepository.save(updateUser);

        UserAccount userAccount = accountRepository.findByUser_ProviderAndUser_Email(updateUser.getProvider(), updateUser.getEmail())
                .orElseThrow(() -> new RestApiException(UserErrorCode.ACCOUNT_NOT_FOUND));
        userAccount.updateAccount(bank, accountNumber);

        accountRepository.save(userAccount);
    }

    //회원 탈퇴
    @Override
    public void deleteUser(String provider, String email) {
        User deleteUser = searchUser(provider, email);
        userRepository.delete(deleteUser);
    }

    //DB에서 회원 조회(소셜 도메인, 이메일로 검색)
    @Override
    public User searchUser(String provider, String email) {
        return userRepository.findByProviderAndEmail(Provider.valueOf(provider), email)
                .orElseThrow(()->new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }
}
