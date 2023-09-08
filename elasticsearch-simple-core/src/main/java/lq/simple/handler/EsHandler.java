package lq.simple.handler;


import com.alibaba.fastjson.JSONObject;
import lq.simple.bean.resp.RestResp;
import lq.simple.client.EsClient;
import lq.simple.core.cover.EsCover;
import lq.simple.core.cover.EsCoverHandler;
import lq.simple.core.dsl.EsDsl;
import lq.simple.core.dsl.EsDslHandler;
import lq.simple.core.ltr.EsLtr;
import lq.simple.core.ltr.EsLtrHandler;
import lq.simple.exception.IndexException;
import lq.simple.result.SearchResult;
import lq.simple.util.JsonCheckUtils;


/**
 * es处理程序
 *
 * @author lqscr88
 * @date 2023/08/30
 */

public class EsHandler extends AbstractEsHandler implements EsCover {

    public EsHandler(String ip, Integer port, String username, String password) {
        esCoverHandler = new EsCoverHandler();
        client = new EsClient(ip,port,username, password).getClient();
    }


    @Override
    public Object setIndex(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.INDEX_ERROR_MESSAGE);
        }
        return cover(super.setIndex(index, json).toString());
    }


    @Override
    public Object setMapping(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.SETTINGS_ERROR_MESSAGE);
        }
        return cover(super.setMapping(index, json).toString());
    }

    @Override
    public Object setSetting(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.SETTINGS_ERROR_MESSAGE);
        }
        return cover(super.setSetting(index, json).toString());
    }

    @Override
    public RestResp<SearchResult> search(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.DSL_ERROR_MESSAGE);
        }
        return super.search(index, json);
    }

    /**
     * 转换
     *
     * @param response 响应
     * @return {@link JSONObject}
     */
    @Override
    public JSONObject cover(String response) {
        return JSONObject.parseObject(response);
    }


    @Override
    public Object getIndex(String index) {
        return cover(super.getIndex(index).toString());
    }


    @Override
    public EsDsl dslOps() {
        return new EsDslHandler();
    }

    @Override
    public EsLtr ltrOps() {
        return new EsLtrHandler(client);
    }
}
