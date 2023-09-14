package lq.simple.plus.annotation;

import java.lang.annotation.*;


/**
 * 索引相关
 *
 * @author lqscr88
 * @date 2023/09/14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IndexName {

    /**
     * 索引对应的名称
     */
    String value() default "";
}
