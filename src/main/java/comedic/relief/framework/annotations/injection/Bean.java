package comedic.relief.framework.annotations.injection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Bean {
    String scope() default "singleton";
}
