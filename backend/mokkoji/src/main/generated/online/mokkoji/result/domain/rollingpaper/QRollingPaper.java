package online.mokkoji.result.domain.rollingpaper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRollingPaper is a Querydsl query type for RollingPaper
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRollingPaper extends EntityPathBase<RollingPaper> {

    private static final long serialVersionUID = 1598124088L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRollingPaper rollingPaper = new QRollingPaper("rollingPaper");

    public final QBackgroundTemplate backgroundTemplate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isEdited = createBoolean("isEdited");

    public final ListPath<Message, QMessage> messages = this.<Message, QMessage>createList("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    public final QPostitTemplate postitTemplate;

    public final online.mokkoji.result.domain.QResult result;

    public QRollingPaper(String variable) {
        this(RollingPaper.class, forVariable(variable), INITS);
    }

    public QRollingPaper(Path<? extends RollingPaper> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRollingPaper(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRollingPaper(PathMetadata metadata, PathInits inits) {
        this(RollingPaper.class, metadata, inits);
    }

    public QRollingPaper(Class<? extends RollingPaper> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.backgroundTemplate = inits.isInitialized("backgroundTemplate") ? new QBackgroundTemplate(forProperty("backgroundTemplate")) : null;
        this.postitTemplate = inits.isInitialized("postitTemplate") ? new QPostitTemplate(forProperty("postitTemplate")) : null;
        this.result = inits.isInitialized("result") ? new online.mokkoji.result.domain.QResult(forProperty("result"), inits.get("result")) : null;
    }

}

