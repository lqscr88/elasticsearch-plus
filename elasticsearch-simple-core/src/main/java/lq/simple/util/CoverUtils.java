package lq.simple.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lq.simple.bean.resp.RestResp;
import lq.simple.enums.SearchKeyWordEnum;
import lq.simple.result.SearchResult;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;


import java.io.IOException;
import java.util.Objects;

public class CoverUtils {

    public static RestResp<SearchResult> coverResult(Response response){
        RestResp<SearchResult> rs = new RestResp<>();
        try {
            JSONObject data = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONObject hits = data.getJSONObject("hits");
            rs.setData(parseResult(hits.getJSONArray("hits"), data.getJSONObject("aggregations")));
            rs.setCount(hits.getJSONObject("total").getInteger("value"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;

    }

    private static SearchResult parseResult(JSONArray hists, JSONObject aggregations) {
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
}
