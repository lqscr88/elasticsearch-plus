package lq.simple.plus.annotation;

import lq.simple.plus.enums.ParticipleImplementTypeEnum;
import lq.simple.plus.enums.ParticipleTypeEnum;

import java.lang.annotation.*;


/**
 * 索引相关
 *
 * @author lqscr88
 * @date 2023/09/14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParticipleType {


    /**
     * 分词器类型
     *
     * @return {@link String}
     */
    ParticipleImplementTypeEnum participleImplementType() default ParticipleImplementTypeEnum.IK_MAX_WORD;

    /**
     * 分词类型
     *
     * @return {@link String}
     */
    ParticipleTypeEnum[] participleType() default ParticipleTypeEnum.KEYWORD;
}
