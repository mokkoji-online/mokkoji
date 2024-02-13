package online.mokkoji.result.domain.rollingpaper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostitTemplate is a Querydsl query type for PostitTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostitTemplate extends EntityPathBase<PostitTemplate> {

    private static final long serialVersionUID = -584052458L;

    public static final QPostitTemplate postitTemplate = new QPostitTemplate("postitTemplate");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final EnumPath<PostitName> postitName = createEnum("postitName", PostitName.class);

    public final StringPath postitPath = createString("postitPath");

    public QPostitTemplate(String variable) {
        super(PostitTemplate.class, forVariable(variable));
    }

    public QPostitTemplate(Path<? extends PostitTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostitTemplate(PathMetadata metadata) {
        super(PostitTemplate.class, metadata);
    }

}

