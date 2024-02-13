package online.mokkoji.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultErrorCode implements ErrorCode {

    NONE_FOLDER_DIRECTORY(400, "잘못된 폴더 경로입니다"),
    RESULT_NOT_FOUND(404, "존재하지 않는 결과물입니다"),
    BACKGROUND_NOT_FOUND(404, "존재하지 않는 배경 템플릿입니다"),
    POSTIT_NOT_FOUND(404, "존재하지 않는 포스트잇 템플릿입니다"),
    ROLLINGPAPER_NOT_FOUND(404,"롤링 페이퍼를 찾을 수 없습니다"),
    PHOTOMOSAIC_NOT_FOUND(404, "포토 모자이크를 찾을 수 없습니다"),
    THUMBNAIL_NOT_FOUND(404, "대표 사진이 설정되지 않았습니다"),
    PHOTO_NOT_FOUND(404, "존재하지 않는 사진입니다"),
    ALREADY_RECOLLECTION(409, "이미 추억으로 전환된 결과물입니다.");

    private final Integer errorCode;
    private final String errorMessage;
}

