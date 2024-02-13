package online.mokkoji.common.exception.errorcode;

import org.springframework.http.HttpStatus;

//에러 코드 정의 추상화를 위한 인터페이스
public interface ErrorCode {
    String name();
    Integer getErrorCode();
    String getErrorMessage();
}
