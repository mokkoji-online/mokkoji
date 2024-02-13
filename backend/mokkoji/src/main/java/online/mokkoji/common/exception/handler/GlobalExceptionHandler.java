package online.mokkoji.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.CommonErrorCode;
import online.mokkoji.common.exception.errorcode.ErrorCode;
import online.mokkoji.common.exception.dto.response.ErrorResDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

//전역적으로 에러를 처리해주는 클래스
@RestControllerAdvice(basePackages = "online.mokkoji")
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<ErrorResDto.ValidationError> errors = ex.getFieldErrors().stream()
                .map(ErrorResDto.ValidationError::of)
                .collect(Collectors.toList());

        ErrorResDto response = ErrorResDto.builder()
                .code(400)  // 적절한 오류 코드 지정
                .message("유효성 검사 실패")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getErrorCode())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResDto makeErrorResponse(ErrorCode errorCode) {
        return ErrorResDto.builder()
                .code(errorCode.getErrorCode())
                .message(errorCode.getErrorMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getErrorCode())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResDto makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResDto.builder()
                .code(errorCode.getErrorCode())
                .message(message)
                .build();
    }
}
