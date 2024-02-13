package online.mokkoji.s3;

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

    //url로 다운로드
    String downloadWithUrl(String s3Url);

    //포토모자이크 cellImages 다운로드
    String downloadCellImages(Long resultId);

    //다운로드 presigned 링크 생성(공유)
    String createDownloadUrl(String fileName);

    //포토모자이크 S3로 업로드
    String uploadPhotomosaic(String localPath, Long resultId);
}
