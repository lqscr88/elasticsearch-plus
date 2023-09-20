package lq.simple.core;

import lq.simple.core.dsl.EsDsl;
import lq.simple.core.index.EsIndexOperate;
import lq.simple.core.ltr.EsLtr;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsOperate extends EsSearchOperate,EsCrudOperate  {

    /**
     * dsl操作
     *
     * @return {@link EsDsl}
     */
    EsDsl dslOps();

    /**
     * ltr操作
     *
     * @return {@link EsLtr}
     */
    EsLtr ltrOps();

    /**
     * 索引操作
     *
     * @return {@link EsIndexOperate}
     */
    EsIndexOperate indexOps();
}
