package online.mokkoji.event.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = 639332641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> participantCount = createNumber("participantCount", Integer.class);

    public final online.mokkoji.result.domain.QResult result;

    public final StringPath sessionId = createString("sessionId");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final EnumPath<EventStatus> status = createEnum("status", EventStatus.class);

    public final online.mokkoji.user.domain.QUser user;

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.result = inits.isInitialized("result") ? new online.mokkoji.result.domain.QResult(forProperty("result"), inits.get("result")) : null;
        this.user = inits.isInitialized("user") ? new online.mokkoji.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

