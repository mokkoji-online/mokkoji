package online.mokkoji.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -2074332689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final EnumPath<Authority> authority = createEnum("authority", Authority.class);

    public final StringPath email = createString("email");

    public final ListPath<online.mokkoji.event.domain.Event, online.mokkoji.event.domain.QEvent> events = this.<online.mokkoji.event.domain.Event, online.mokkoji.event.domain.QEvent>createList("events", online.mokkoji.event.domain.Event.class, online.mokkoji.event.domain.QEvent.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final QRecord record;

    public final StringPath refreshToken = createString("refreshToken");

    public final ListPath<online.mokkoji.result.domain.Result, online.mokkoji.result.domain.QResult> results = this.<online.mokkoji.result.domain.Result, online.mokkoji.result.domain.QResult>createList("results", online.mokkoji.result.domain.Result.class, online.mokkoji.result.domain.QResult.class, PathInits.DIRECT2);

    public final QUserAccount userAccount;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.record = inits.isInitialized("record") ? new QRecord(forProperty("record"), inits.get("record")) : null;
        this.userAccount = inits.isInitialized("userAccount") ? new QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
    }

}

