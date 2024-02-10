package online.mokkoji.result.service;

import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.dto.request.RollingPaperReqDto;
import online.mokkoji.result.dto.response.MessageResDto;
import online.mokkoji.result.dto.response.ResultResDto;
import online.mokkoji.result.dto.response.RollingpaperEditResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ResultService {
    //create, read, update, delete
    Map<String, Object> getResultList(String provider, String email);

    ResultResDto getResult(Long resultId, Pageable pageable);

    void createRecollection(Long resultId);

    // 사진 db 저장
    void createPhoto(PhotoResDto photoResDto);

    // 메시지 db 저장
    void createMessage(MessageResDto message);

    // 기억 편집 화면에서 필요한 사진 url 가져옴
    List<String> getPhotoPath(Long resultId);

    // 기억 편집 화면에서 롤링페이퍼 템플릿 가져옴
    RollingpaperEditResDto getRollingpaperTemplate(Long resultId);

    // 롤링페이퍼 템플릿 변경
    void updateRollingpaper(Long resultId, RollingPaperReqDto rollingPaperReqDto);

    // 대표이미지 설정
    void updateThumbnail(Long resultId, String url);

    // 사진첩 사진 추가
    void createPhotoList(List<PhotoResDto> photoResDtoList);

    // 사진 url redis에 업데이트
    List<String> updatePhotoPathCache(Long resultId, List<PhotoResDto> photoResDtoList);
}
