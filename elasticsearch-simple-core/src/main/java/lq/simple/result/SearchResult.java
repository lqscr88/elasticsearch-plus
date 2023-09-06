package lq.simple.result;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SearchResult {

    private JSONArray result;
    private List<Agg> agg;
    private String logId;

    private JSONObject ext;
    private Object fieldConfig;

    public SearchResult(JSONArray result) {
        this.result = result;
    }

    public void addAgg(String name,JSONArray data){
        if(agg == null){
            agg = new ArrayList<>();
        }
        agg.add(new Agg(name,data.toJavaList( JSONObject.class )));
    }

    @Setter
    @Getter
    public static class Agg{

        String name;
        String matchMethod;
        List<JSONObject> data;
        String fieldName;
        String format;
        String type;
        String aggConf;

        public Agg(String name, List<JSONObject> data) {
            this.name = name;
            this.data = data;
        }
    }
}
