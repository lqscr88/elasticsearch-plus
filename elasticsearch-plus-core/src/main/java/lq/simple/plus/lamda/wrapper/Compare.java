package lq.simple.plus.lamda.wrapper;

import java.io.Serializable;
import java.util.Map;

public interface Compare<Children, R> extends Serializable {
    /**
     * ignore
     */
    default Children match(R column, Object val) {
        return match(true, column, val,1f);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children match(boolean condition, R column, Object val,Float boost);



    /**
     * 起始页
     *
     * @param val       值
     * @return children
     */
    Children from(Integer val);




    /**
     * 总大小
     *
     * @param val       值
     * @return children
     */
    Children size(Integer val);


    /**
     * 分词器
     *
     * @param val       值
     * @return children
     */
    Children analyzer(String val);


    /**
     * 索引
     *
     * @param val       值
     * @return children
     */
    Children index(String val);




}
