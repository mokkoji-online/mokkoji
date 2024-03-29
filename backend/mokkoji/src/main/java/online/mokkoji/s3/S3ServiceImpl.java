package online.mokkoji.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.EventErrorCode;
import online.mokkoji.common.exception.errorcode.ResultErrorCode;
import online.mokkoji.common.exception.errorcode.S3ErrorCode;
import online.mokkoji.common.exception.errorcode.UserErrorCode;
import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.domain.Photo;
import online.mokkoji.result.domain.Result;
import online.mokkoji.result.repository.PhotoRepository;
import online.mokkoji.result.repository.ResultRepository;
import online.mokkoji.user.domain.Provider;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String LOCAL_PATH = System.getProperty("user.home") + File.separator + "Desktop"
            + File.separator + "mokkoji" + File.separator;

    private final AmazonS3Client amazonS3Client;
    private final ResultRepository resultRepository;
    private final PhotoRepository photoRepository;

    /**
     * 사진 한 장 업로드
     * @param multipartFile 프론트에서 넘겨준 파일
     * @param userId
     * @param result
     * @throws IOException
     */
    @Override
    public PhotoResDto uploadOnePhoto(MultipartFile multipartFile, Long userId, Result result) throws IOException {
        return getPhotoResDto(multipartFile, userId, result);
    }

    /**
     * 사진 여러 장 업로드
     * @param photoList
     * @param userId
     * @param resultId
     * @return
     */
    @Override
    public List<PhotoResDto> uploadPhotoList(List<MultipartFile> photoList, Long userId, Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.RESULT_NOT_FOUND));

        List<PhotoResDto> dtoList = new ArrayList<>();

        for (MultipartFile photo : photoList) {
            try {
                PhotoResDto photoResDto = getPhotoResDto(photo, userId, result);
                dtoList.add(photoResDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return dtoList;
    }


    /**
     * 롤링페이퍼 업로드
     * @param multipartFiles
     * @param userId
     * @param resultId
     * @return S3 객체 키, 객체 URL을 담은 Map
     * @throws IOException
     */
    @Override
    public Map<String, String> uploadRollingpaper(Map<String, MultipartFile> multipartFiles, Long userId, Long resultId) throws IOException {
        String dir = "rollingpaper";
        String subDir = "";
        String prefix;
        Map<String, String> urlMap = new HashMap<>();

        for (Map.Entry<String, MultipartFile> fileEntry : multipartFiles.entrySet()) {
            // 음성, 비디오 메세지 구분
            if (fileEntry.getKey().equals("voice")) {
                prefix = "voi_";
            } else {
                prefix = "vid_";
            }

            MultipartFile multipartFile = fileEntry.getValue();

            // 사진_유저ID_결과물ID
            String fileName = createFileName(userId.toString(), resultId.toString(), dir, subDir, prefix, multipartFile.getOriginalFilename());

            upload(multipartFile, fileName);


            urlMap.put(fileEntry.getKey(), amazonS3Client.getUrl(bucket, fileName).toString());

        }
        return urlMap;
    }

    /**
     * 대표 이미지 외 다른 이미지 삭제
     * @param resultId
     */
    @Override
    @CacheEvict(value = "photoPath", key = "#resultId", cacheManager = "cacheManager")
    public void deletePhotos(Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.RESULT_NOT_FOUND));

        List<Photo> photos = result.getPhotos();

        for (Photo photo : photos) {
            if (!result.getImage().equals(photo.getPhotoPath())) {
                // S3에서 삭제
                delete(getPhotoKey(photo.getPhotoPath()));
                // db에서 삭제
                photoRepository.deleteById(photo.getId());
            }
        }
    }

    /**
     * 경로에서 key값 반환
     * @param photoPath
     * @return S3 객체 키
     */
    private String getPhotoKey(String photoPath) {
        String prefixToRemove="https://mokkoji-bucket.s3.ap-northeast-2.amazonaws.com/";

        return photoPath.substring(prefixToRemove.length());
    }


    /**
     * 업로드한 사진 정보 Dto로 반환
     * @param multipartFile
     * @param userId
     * @param result
     * @return PhotoResDto, result, 사진 경로 반환
     * @throws IOException
     */
    private PhotoResDto getPhotoResDto(MultipartFile multipartFile, Long userId, Result result) throws IOException {
        Long resultId= result.getId();
        String dir = "photos";
        String subDir = "photoList";
        String prefix = "pic_";
        String fileName = createFileName(userId.toString(), resultId.toString(), dir, subDir, prefix, multipartFile.getOriginalFilename());

        upload(multipartFile, fileName);

        return new PhotoResDto(result, amazonS3Client.getUrl(bucket, fileName).toString());
    }

    /**
     * S3 사진 삭제
     * @param photoKey S3 객체 키
     */
    private void delete(String photoKey) {
        if (amazonS3Client.doesObjectExist(bucket, photoKey)) {
            try {
                amazonS3Client.deleteObject(bucket, photoKey);
            } catch (Exception e) {
                log.debug("S3 사진 삭제 실패", e);
                e.printStackTrace();
            }
        } else {
            throw new RestApiException(ResultErrorCode.PHOTO_NOT_FOUND);
        }
    }

    /**
     * S3에 파일 업로드
     * @param multipartFile 업로드 사진
     * @param fileName S3 객체명
     * @throws IOException
     */
    private void upload(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata);

        try {
            amazonS3Client.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * S3 객체 파일명 생성
     * @param userId
     * @param resultId
     * @param dir S3 경로
     * @param subDir S3 하위 경로
     * @param prefix 객체 유형
     * @param fileName 파일 이름
     * @return 파일명
     */
    private String createFileName(String userId, String resultId, String dir, String subDir, String prefix, String fileName) {
        if (subDir.isBlank()) return String.format("%s/%s/%s/%s%s%s",
                userId, resultId, dir, prefix, UUID.randomUUID(), getFileExtension(fileName));
        return String.format("%s/%s/%s/%s/%s%s%s",
                userId, resultId, dir, subDir, prefix, UUID.randomUUID(), getFileExtension(fileName));
    }

    /**
     * 확장자 유무 검사
     * @param fileName 파일 이름
     * @return 확장자 제외한 파일명
     */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new RestApiException(EventErrorCode.FILE_EXTENSION_NOT_FOUND);
        }
    }

    /**
     * S3 URL로 파일 다운로드
     * @param s3Url
     * @param folderName sub_dir명
     * @return 로컬 저장 경로
     */
    @Override
    public String downloadWithUrl(String s3Url, String folderName) {
        URL url;
        try {
            url = new URL(s3Url);
        } catch (MalformedURLException e) {
            throw new RestApiException(S3ErrorCode.INVALID_URL);
        }

        //파일명 유일성을 위해 다운로드 시간으로 생성
        String key = url.getPath().substring(1);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMdd_HHmmss");
        String fileName = dateTime.format(format);

        File localFile = new File(LOCAL_PATH + folderName + File.separator + fileName
                + key.substring(key.lastIndexOf('.')));

        //파일 경로 유무 확인, 생성
        File parentDir = localFile.getParentFile();
        if(!parentDir.exists()) {
            parentDir.mkdirs();
        }

        //S3 객체 접근, localFile로 저장
        GetObjectRequest request = new GetObjectRequest(bucket, key);
        amazonS3Client.getObject(request, localFile);

        return localFile.getAbsolutePath();
    }

    /**
     * 대표사진 로컬로 다운로드
     * @param resultId
     * @param thumbnailPath 대표사진 경로
     */
    @Override
    public void downloadThumbnail(Long resultId, String thumbnailPath) {
        URL url;
        try {
            url = new URL(thumbnailPath);
        } catch (MalformedURLException e) {
            throw new RestApiException(S3ErrorCode.INVALID_URL);
        }

        String key = url.getPath().substring(1);

        File localFile = new File(LOCAL_PATH + resultId + File.separator + "thumbnail.jpg");

        File parentDir = localFile.getParentFile();
        if(!parentDir.exists()) {
            parentDir.mkdirs();
        }

        GetObjectRequest request = new GetObjectRequest(bucket, key);

        amazonS3Client.getObject(request, localFile);
    }


    /**
     * S3 폴더 하위 사진 모두 다운로드
     * @param resultId
     */
    @Override
    public void downloadCellImages(Long resultId) {
        Result findResult = resultRepository.findById(resultId)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.RESULT_NOT_FOUND));

        Long userId = findResult.getUser().getId();

        String folderPrefix = userId + "/" + resultId + "/" +
                "photos/photoList";

        //S3 버킷명, 폴더 경로로 객체 list 요청
        ListObjectsV2Request listRequest = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(folderPrefix);

        ListObjectsV2Result listResponse = amazonS3Client.listObjectsV2(listRequest);

        //로컬 저장 경로 생성
        Path cellImagesPath = Paths.get(LOCAL_PATH + resultId + File.separator + "cellImages");
        if(!Files.exists(cellImagesPath)) {
            try {
                Files.createDirectories(cellImagesPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //S3 객체 다운로드
        for(S3ObjectSummary s3ObjectSummary : listResponse.getObjectSummaries()) {
            String key = s3ObjectSummary.getKey();

            File cellImage = new File(cellImagesPath + File.separator + key.substring(key.lastIndexOf('/') + 1));

            GetObjectRequest request = new GetObjectRequest(bucket, key);

            amazonS3Client.getObject(request, cellImage);
        }
    }

    /**
     * 포토 모자이크 S3로 업로드
     * @param localPath 로컬 저장 경로
     * @param resultId
     * @return S3 포토 모자이크 객체 URL
     */
    @Override
    public String uploadPhotomosaic(String localPath, Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.RESULT_NOT_FOUND));

        String key = result.getUser().getId() + "/" + resultId + "/photos/photomosaic.jpg";

        //S3에 기존 포토 모자이크가 존재하면 삭제
        if (amazonS3Client.doesObjectExist(bucket, key)) {
            amazonS3Client.deleteObject(bucket, key);
        }

        //재생성
        File photomosaic = new File(LOCAL_PATH + resultId + File.separator + "photomosaic.jpg");

        PutObjectRequest request = new PutObjectRequest(bucket, key, photomosaic);

        try {
            //S3 업로드
            amazonS3Client.putObject(request);
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }
}
