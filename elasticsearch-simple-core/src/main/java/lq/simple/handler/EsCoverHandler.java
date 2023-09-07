package lq.simple.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lq.simple.bean.resp.RestResp;
import lq.simple.core.cover.AbstractEsCoverHandler;
import lq.simple.core.cover.EsCover;
import lq.simple.enums.SearchKeyWordEnum;
import lq.simple.result.SearchResult;

import java.util.Objects;


/**
 * es转换处理程序
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public class EsCoverHandler extends AbstractEsCoverHandler{

    /**
     * 解析结果
     *
     * @param hists        结果集
     * @param aggregations 聚合
     * @return {@link SearchResult}
     */
    protected static SearchResult parseResult(JSONArray hists, JSONObject aggregations) {
        JSONArray result = new JSONArray(hists.size());
        for (Object hist : hists) {
            JSONObject obj = (JSONObject) hist;
            JSONObject source = obj.getJSONObject("_source");
            source.put("_index", obj.getString("_index"));
            source.put("_score", obj.getBigDecimal("_score"));
            source.put("_id", obj.getString("_id"));
            //如果高亮有结果,
            if (obj.getJSONObject(SearchKeyWordEnum.highlight.name()) != null) {
                source.put(SearchKeyWordEnum.highlight.name(), obj.getJSONObject(SearchKeyWordEnum.highlight.name()));
            }
            if (obj.containsKey("fields")) {
                source.put("log_entry", obj.getJSONObject("fields").getJSONArray("_ltrlog").getJSONObject(0).getJSONArray("log_entry"));
            }
            result.add(source);

        }
        SearchResult searchResult = new SearchResult(result);
        if (Objects.nonNull(aggregations)) {
            for (String k : aggregations.keySet()) {
                if (k.equalsIgnoreCase("all_index")) {
                    searchResult.addAgg(k, aggregations.getJSONObject(k).getJSONArray("buckets"));
                } else {
                    if (!aggregations.getJSONObject(k).getJSONArray("buckets").isEmpty()) {
                        searchResult.addAgg(k, aggregations.getJSONObject(k).getJSONArray("buckets"));
                    }
                }

            }
        }
        return searchResult;
    }

    @Override
    protected RestResp<SearchResult> doCover(String response) {
        RestResp<SearchResult> rs = new RestResp<>();
        JSONObject data = JSONObject.parseObject(response);
        JSONObject hits = data.getJSONObject("hits");
        rs.setData(parseResult(hits.getJSONArray("hits"), data.getJSONObject("aggregations")));
        rs.setCount(hits.getJSONObject("total").getInteger("value"));
        return rs;
    }
}
