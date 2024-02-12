package online.mokkoji.S3;

import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.domain.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface S3Service {

    // 사진 업로드
    PhotoResDto uploadOnePhoto(MultipartFile multipartFile, Long userId, Result result) throws IOException;

    // 사진 여러개 업로드
    List<PhotoResDto> uploadPhotoList(List<MultipartFile> photoList, Long userId, Long resultId);

    // 롤링페이퍼 업로드
    Map<String, String> uploadRollingpaper(Map<String, MultipartFile> multipartFiles, Long userId, Long resultId) throws IOException;

    // 대표이미지 제외 사진 삭제
    void deletePhotos(Long resultId);

    String downloadWithUrl(String s3Url, String provider, String email);

    String downloadAllPhotos(Long resultId, String provider, String email);

    String createDownloadUrl(String fileName);

    String uploadPhotomosaic(MultipartFile multipartFile, Long resultId, String provider, String email);

}
