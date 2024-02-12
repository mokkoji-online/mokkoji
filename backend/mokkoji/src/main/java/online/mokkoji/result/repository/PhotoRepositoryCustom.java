package online.mokkoji.result.repository;

import java.util.List;

public interface PhotoRepositoryCustom {

    List<String> findPhotoPathListByResultId(Long resultId);
}
