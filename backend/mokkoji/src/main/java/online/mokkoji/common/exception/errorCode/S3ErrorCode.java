package online.mokkoji.common.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    INVALID_URL(400,"올바르지 않은 URL 형식입니다"),
    PERMISSION_NOT_GRANTED(403, "접근 권한이 없는 요청입니다");

    private final Integer errorCode;
    private final String errorMessage;
}
