package lq.simple.handler;


import lq.simple.bean.resp.RestResp;
import lq.simple.client.EsClient;
import lq.simple.core.cover.EsCoverHandler;
import lq.simple.core.dsl.EsDsl;
import lq.simple.core.dsl.EsDslHandler;
import lq.simple.core.ltr.EsLtr;
import lq.simple.core.ltr.EsLtrHandler;
import lq.simple.exception.IndexException;
import lq.simple.exception.UpdateException;
import lq.simple.result.SearchResult;
import lq.simple.util.JsonCheckUtils;


/**
 * es处理程序
 *
 * @author lqscr88
 * @date 2023/08/30
 */

public class EsHandler extends AbstractEsHandler{

    public EsHandler(String ip, Integer port, String username, String password) {
        esCoverHandler = new EsCoverHandler();
        client = new EsClient(ip,port,username, password).getClient();
    }


    @Override
    public Object createIndex(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.INDEX_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.createIndex(index, json).toString());
    }


    @Override
    public Object deleteIndex(String index) {
        return esCoverHandler.cover(super.deleteIndex(index).toString());
    }

    @Override
    public Object updateMapping(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.MAPPINGS_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.updateMapping(index, json).toString());
    }

    @Override
    public Object updateSetting(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.SETTINGS_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.updateSetting(index, json).toString());
    }

    @Override
    public RestResp<SearchResult> search(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.DSL_ERROR_MESSAGE);
        }
        return super.search(index, json);
    }

    @Override
    public Object update(String index, String json) {
        if (!json.contains(ID)){
            throw new UpdateException(UpdateException.UPDATE_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.update(index, json).toString());
    }

    @Override
    public Object getIndex(String index) {
        return esCoverHandler.cover(super.getIndex(index).toString());
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
