package online.mokkoji.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {

    INVALID_URL(400,"올바르지 않은 URL 형식입니다"),
    INVALID_FILE_PARAMETER(400, "요청으로 전달된 파일이 존재하지 않습니다"),
    PERMISSION_NOT_GRANTED(403, "접근 권한이 없는 요청입니다");

    private final Integer errorCode;
    private final String errorMessage;
}
