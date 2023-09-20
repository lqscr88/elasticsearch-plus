package lq.simple.plus.annotation;

import lq.simple.plus.constant.IndexSettingsConstant;

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
public @interface IndexSettings {

    /**
     * settings配置
     */
    String value() default IndexSettingsConstant.DEFULT;

    /**
     * 分片数量
     *
     * @return int
     */
    int shardsNumber() default 1;

    /**
     * 副本数量
     *
     * @return int
     */
    int replicasNumber() default 0;
}
