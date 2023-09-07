package lq.simple.core.ltr;

import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.util.CharacterUtil;
import lq.simple.util.ResultUtil;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public abstract class AbstractEsLtrHandler implements EsLtr {

    protected RestHighLevelClient client;
    /**
     * init ltr
     *
     * @return {@link Object}
     */
    @Override
    public Object initLtr() {
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat("_ltr"));
        return ResultUtil.getResult(ResultUtil.getResponse(request,client));
    }
}
