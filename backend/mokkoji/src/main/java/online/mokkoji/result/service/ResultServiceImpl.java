package online.mokkoji.result.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorCode.ResultErrorCode;
import online.mokkoji.common.exception.errorCode.UserErrorCode;
import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.domain.Photo;
import online.mokkoji.result.domain.Photomosaic;
import online.mokkoji.result.domain.Result;
import online.mokkoji.result.domain.RollingPaper.*;
import online.mokkoji.result.dto.request.RollingPaperReqDto;
import online.mokkoji.result.dto.response.*;
import online.mokkoji.result.repository.*;
import online.mokkoji.user.domain.Provider;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final PhotoRepository photoRepository;
    private final MessageRepository messageRepository;
    private final RollingPaperRepository rollingPaperRepository;
    private final BackgroundTemplateRepository backgroundTemplateRepository;
    private final PostitTemplateRepository postitTemplateRepository;
    private final UserRepository userRepository;

    // 행사 리스트
    @Override
    public Map<String, Object> getResultList(String provider, String email) {

        User user = userRepository.findByProviderAndEmail(Provider.valueOf(provider), email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        List<Result> resultList = user.getResults();

        if (resultList.isEmpty())
            return null;

        Map<String, Object> resultMap = new HashMap<>();
        List<MemoryInfoResDto> memoryList = new ArrayList<>();
        List<RecollectionInfoResDto> recollectionList = new ArrayList<>();

        for (Result result : resultList) {
            LocalDate date = result.getEvent().getStartTime().toLocalDate();

            if (result.getStatus().getKey().equals("memory")) {
                MemoryInfoResDto memoryInfoResDto = MemoryInfoResDto.builder()
                        .resultId(result.getId())
                        .date(date)
                        .participantCount(result.getEvent().getParticipantCount())
                        .isPaperEdited(result.getRollingpaper().isEdited())
                        .isMosaicCreated(result.getPhotomosaic() != null)
                        .build();

                memoryList.add(memoryInfoResDto);
                continue;
            }

            RecollectionInfoResDto recollectionInfoResDto = RecollectionInfoResDto.builder()
                    .resultId(result.getId())
                    .date(date)
                    .image(result.getImage())
                    .name(result.getName())
                    .content(result.getContent())
                    .build();

            recollectionList.add(recollectionInfoResDto);
        }

        resultMap.put("memoryList", memoryList);
        resultMap.put("recollectionList", recollectionList);
        return resultMap;
    }

    // 롤링페이퍼와 메시지 페이징
    @Override
    public ResultResDto getResult(Long resultId, Pageable pageable) {
        Optional<Result> findResult = resultRepository.findById(resultId);

        if(findResult.isEmpty())
            throw new RestApiException(ResultErrorCode.RESULT_NOT_FOUND);

        Result result = findResult.get();

        RollingPaper rollingPaper = result.getRollingpaper();

        if(rollingPaper == null)
            throw new RestApiException(ResultErrorCode.ROLLINGPAPER_NOT_FOUND);

        // TODO : @Cacheable(value = "messages", key = "#rollingpaperId + #pageNo") 를 하고 싶은데 pageNo를 어케 하는지
        Page<Message> messageList = messageRepository.findAllByRollingPaper_Id(rollingPaper.getId(), pageable);

        Photomosaic photomosaic = result.getPhotomosaic();

        return ResultResDto.builder()
                .backgroundTemplate(rollingPaper.getBackgroundTemplate().getBackgroundPath())
                .postitTemplate(rollingPaper.getPostitTemplate().getPostitPath())
                .messageList(messageList)
                .photomosaic(photomosaic == null ? "" : photomosaic.getPath())
                .build();
    }

    @Override
    public void createRecollection(Long resultId) {
        Optional<Result> findResult = resultRepository.findById(resultId);

        if(findResult.isEmpty())
            throw new RestApiException(ResultErrorCode.RESULT_NOT_FOUND);

        Result result = findResult.get();

        if(result.getStatus().getKey().equals("recollection"))
            throw new RestApiException(ResultErrorCode.ALREADY_RECOLLECTION);

        result.updateStatus();
        resultRepository.save(result);
    }

    // 사진 db 저장
    @Override
    public void createPhoto(PhotoResDto photoResDto) {

        Photo photo = Photo.builder().result(photoResDto.getResult()).photoPath(photoResDto.getPhotoPath()).build();
        photoRepository.save(photo);
    }


    // 메시지 db 저장
    @Override
    public void createMessage(MessageResDto messageResDto) {
        Long paperId = messageResDto.getPaperId();

        RollingPaper rollingPaper = rollingPaperRepository.getReferenceById(paperId);

        Message message = Message.builder()
                .rollingPaper(rollingPaper)
                .writer(messageResDto.getWriter())
                .text(messageResDto.getText())
                .voicePath(messageResDto.getVoicePath())
                .videoPath(messageResDto.getVideoPath())
                .build();
        messageRepository.save(message);
    }


    // 기억 편집 화면에서 필요한 사진 url 가져옴
    @Override
    @Cacheable(value = "photoPath", key = "#resultId", cacheManager = "cacheManager")
    public List<String> getPhotoPath(Long resultId) {
        return photoRepository.findPhotoPathListByResultId(resultId);
    }

    // 기억 편집 화면에서 롤링페이퍼 템플릿 가져옴
    public RollingpaperEditResDto getRollingpaperTemplate(Long resultId) {

        // result를 통해 롤링페이퍼 가져옴
        Result result = getResultById(resultId);
        RollingPaper rollingPaper = result.getRollingpaper();

        // 롤링페이퍼 설정된 템플릿 가져옴
        RollingpaperEditResDto rollingpaperDto = new RollingpaperEditResDto(rollingPaper.getBackgroundTemplate(), rollingPaper.getPostitTemplate());

        return rollingpaperDto;
    }

    // 롤링페이퍼 템플릿 변경
    @Override
    public void updateRollingpaper(Long resultId, RollingPaperReqDto rollingPaperReqDto) {
        Result result = getResultById(resultId);
        RollingPaper rollingpaper = result.getRollingpaper();

        // 요청 들어온 이름에 맞는 템플릿 찾기
        BackgroundName backgroundName = BackgroundName.valueOf(rollingPaperReqDto.getBackgroundName().toUpperCase());
        BackgroundTemplate backgroundTemplate = backgroundTemplateRepository.findByBackgroundName(backgroundName)
                .orElseThrow(()->new RestApiException(ResultErrorCode.BACKGROUND_NOT_FOUND));

        PostitName postitName = PostitName.valueOf(rollingPaperReqDto.getPostitName().toUpperCase());
        PostitTemplate postitTemplate = postitTemplateRepository.findByPostitName(postitName)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.POSTIT_NOT_FOUND));

        rollingpaper.updateTemplate(backgroundTemplate,postitTemplate);

        // 변경내용 수정
        rollingPaperRepository.save(rollingpaper);
    }

    // 대표이미지 설정
    @Override
    public void updateThumbnail(Long resultId, String url) {
        Result result = getResultById(resultId);
        result.setImage(url);
        resultRepository.save(result);
    }

    // 사진 여러개 저장(1개도 가능)
    @Override
    public void createPhotoList(List<PhotoResDto> photoResDtoList) {

        for (PhotoResDto photoResDto : photoResDtoList) {
            // 하나씩 db에 저장
            createPhoto(photoResDto);
        }
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "photoPath", key = "#resultId", cacheManager = "cacheManager")},
            put = {@CachePut(value = "photoPath", key = "#resultId", cacheManager = "cacheManager")}
    )
    public List<String> updatePhotoPathCache(Long resultId, List<PhotoResDto> photoResDtoList) {

        List<String> photoPath = getPhotoPath(resultId);
        for (PhotoResDto photoResDto : photoResDtoList) {
            photoPath.add(photoResDto.getPhotoPath());
        }

        return photoPath;
    }

    // 결과객체 가져오는 메서드
    private Result getResultById(Long resultId) {
        return resultRepository.findById(resultId)
                .orElseThrow(() -> new RestApiException(ResultErrorCode.RESULT_NOT_FOUND));
    }


}
