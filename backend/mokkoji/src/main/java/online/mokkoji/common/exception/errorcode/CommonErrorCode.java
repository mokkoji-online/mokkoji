package online.mokkoji.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

//전역 에러코드 정의
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(400, "잘못된 매개변수가 포함되었습니다."),
    RESOURCE_NOT_FOUND(404, "요청 내용을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다.");

    private final Integer errorCode;
    private final String errorMessage;
}
