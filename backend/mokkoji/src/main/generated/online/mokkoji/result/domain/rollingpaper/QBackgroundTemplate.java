package online.mokkoji.result.domain.rollingpaper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBackgroundTemplate is a Querydsl query type for BackgroundTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackgroundTemplate extends EntityPathBase<BackgroundTemplate> {

    private static final long serialVersionUID = -728820487L;

    public static final QBackgroundTemplate backgroundTemplate = new QBackgroundTemplate("backgroundTemplate");

    public final EnumPath<BackgroundName> backgroundName = createEnum("backgroundName", BackgroundName.class);

    public final StringPath backgroundPath = createString("backgroundPath");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QBackgroundTemplate(String variable) {
        super(BackgroundTemplate.class, forVariable(variable));
    }

    public QBackgroundTemplate(Path<? extends BackgroundTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBackgroundTemplate(PathMetadata metadata) {
        super(BackgroundTemplate.class, metadata);
    }

}

