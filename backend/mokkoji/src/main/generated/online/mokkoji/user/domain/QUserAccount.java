package online.mokkoji.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAccount is a Querydsl query type for UserAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAccount extends EntityPathBase<UserAccount> {

    private static final long serialVersionUID = 1308603710L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAccount userAccount = new QUserAccount("userAccount");

    public final StringPath bank = createString("bank");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath number = createString("number");

    public final QUser user;

    public QUserAccount(String variable) {
        this(UserAccount.class, forVariable(variable), INITS);
    }

    public QUserAccount(Path<? extends UserAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAccount(PathMetadata metadata, PathInits inits) {
        this(UserAccount.class, metadata, inits);
    }

    public QUserAccount(Class<? extends UserAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

