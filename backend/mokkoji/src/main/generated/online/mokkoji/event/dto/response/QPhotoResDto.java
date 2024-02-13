package online.mokkoji.event.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * online.mokkoji.event.dto.response.QPhotoResDto is a Querydsl Projection type for PhotoResDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPhotoResDto extends ConstructorExpression<PhotoResDto> {

    private static final long serialVersionUID = 1263177508L;

    public QPhotoResDto(com.querydsl.core.types.Expression<? extends StringPath> photoPath) {
        super(PhotoResDto.class, new Class<?>[]{StringPath.class}, photoPath);
    }

}

