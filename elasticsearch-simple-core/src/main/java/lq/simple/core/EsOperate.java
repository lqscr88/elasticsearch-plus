package lq.simple.core;

import lq.simple.bean.req.QueryReq;
import lq.simple.bean.resp.RestResp;
import lq.simple.core.dsl.EsDsl;
import lq.simple.core.ltr.EsLtr;
import lq.simple.result.SearchResult;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsOperate extends EsSearchOperate,EsIndexOperate,EsCrudOperate  {

    EsDsl dslOps();

    EsLtr ltrOps();
}
