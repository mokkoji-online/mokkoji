package online.mokkoji.result.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhotomosaic is a Querydsl query type for Photomosaic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhotomosaic extends EntityPathBase<Photomosaic> {

    private static final long serialVersionUID = 1701804102L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhotomosaic photomosaic = new QPhotomosaic("photomosaic");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath path = createString("path");

    public final QResult result;

    public QPhotomosaic(String variable) {
        this(Photomosaic.class, forVariable(variable), INITS);
    }

    public QPhotomosaic(Path<? extends Photomosaic> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhotomosaic(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhotomosaic(PathMetadata metadata, PathInits inits) {
        this(Photomosaic.class, metadata, inits);
    }

    public QPhotomosaic(Class<? extends Photomosaic> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.result = inits.isInitialized("result") ? new QResult(forProperty("result"), inits.get("result")) : null;
    }

}

