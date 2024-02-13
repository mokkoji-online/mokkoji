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

    @Override
    public UpdatePageResDto getUpdatePage(String provider, String email) {
        Optional<User> findUser = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email);

        if (findUser.isEmpty()) {
            throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        }

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

    @Override
    public MyPageResDto getMypage(String provider, String email) {
        Optional<User> findUser = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email);

        if (findUser.isEmpty()) {
            throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        }

        User readUser = findUser.get();
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

    @Override
    public void createUser(String provider, String email, UserInputReqDto userInputReqDto) {
        Optional<User> findUser = userRepository.findByProviderAndEmail
                (Provider.valueOf(provider), email);

        if(findUser.isPresent() && findUser.get().getAuthority().getKey().equals("ROLE_USER")) {
            throw new RestApiException(UserErrorCode.DUPLICATE_SIGNUP);
        }

        String refreshToken = jwtService.createRefreshToken();

        User newUser = findUser.get();
        newUser.updateAuthority();
        newUser.updateRefreshToken(refreshToken);
        userRepository.save(newUser);

        Record record = Record.builder()
                .user(newUser)
                .build();
        recordRepository.save(record);

        String bank = userInputReqDto.getBank();
        String accountNumber = userInputReqDto.getAccountNumber();

        if(!bank.equals("") && !accountNumber.equals("")) {
            UserAccount userAccount = UserAccount.builder()
                    .user(newUser)
                    .bank(bank)
                    .number(accountNumber)
                    .build();

            accountRepository.save(userAccount);
        }
    }

    @Override
    public void updateUser(String provider, String email, UserInputReqDto modifyDto) {
        Optional<User> findUser = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email);

        if (findUser.isEmpty()) {
            throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        }

        String name = modifyDto.getName();
        String image = modifyDto.getImage();
        String bank = modifyDto.getBank();
        String accountNumber = modifyDto.getAccountNumber();

        User updateUser = findUser.get();
        updateUser.updateUser(name, image);
        userRepository.save(updateUser);

        Optional<UserAccount> findAccount =
                accountRepository.findByUser_ProviderAndUser_Email(updateUser.getProvider(), updateUser.getEmail());

        UserAccount userAccount = findAccount.get();
        userAccount.updateAccount(bank, accountNumber);

        accountRepository.save(userAccount);
    }

    @Override
    public void deleteUser(String provider, String email) {
        Optional<User> findUser = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email);

        if (findUser.isEmpty()) {
            throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        }

        User deleteUser = findUser.get();
        userRepository.delete(deleteUser);
    }

    @Override
    public User getByProviderAndEmail(String provider, String email) {
        return userRepository.findByProviderAndEmail(Provider.valueOf(provider), email)
                .orElseThrow(()->new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

}
