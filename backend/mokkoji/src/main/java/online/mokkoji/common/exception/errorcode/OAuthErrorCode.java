package online.mokkoji.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthErrorCode implements ErrorCode {

    INVALID_AUTHORIZATION_CODE(400, "Authorization code값이 유효하지 않습니다.");

    private final Integer errorCode;
    private final String errorMessage;
}
