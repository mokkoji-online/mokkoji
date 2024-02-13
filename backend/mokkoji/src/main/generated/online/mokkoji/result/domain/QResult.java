package online.mokkoji.result.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResult is a Querydsl query type for Result
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QResult extends EntityPathBase<Result> {

    private static final long serialVersionUID = 976916179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResult result = new QResult("result");

    public final StringPath content = createString("content");

    public final online.mokkoji.event.domain.QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final QPhotomosaic photomosaic;

    public final ListPath<Photo, QPhoto> photos = this.<Photo, QPhoto>createList("photos", Photo.class, QPhoto.class, PathInits.DIRECT2);

    public final online.mokkoji.result.domain.rollingpaper.QRollingPaper rollingpaper;

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final online.mokkoji.user.domain.QUser user;

    public QResult(String variable) {
        this(Result.class, forVariable(variable), INITS);
    }

    public QResult(Path<? extends Result> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResult(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResult(PathMetadata metadata, PathInits inits) {
        this(Result.class, metadata, inits);
    }

    public QResult(Class<? extends Result> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new online.mokkoji.event.domain.QEvent(forProperty("event"), inits.get("event")) : null;
        this.photomosaic = inits.isInitialized("photomosaic") ? new QPhotomosaic(forProperty("photomosaic"), inits.get("photomosaic")) : null;
        this.rollingpaper = inits.isInitialized("rollingpaper") ? new online.mokkoji.result.domain.rollingpaper.QRollingPaper(forProperty("rollingpaper"), inits.get("rollingpaper")) : null;
        this.user = inits.isInitialized("user") ? new online.mokkoji.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

