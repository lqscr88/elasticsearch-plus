package lq.simple.core.ltr.constant;

public class LtrDslConstant {
    public static final  String  LTR_QUERY_DSL = "{\"rescore\":{\"window_size\":1000,\"query\":{\"rescore_query\":{\"sltr\":{\"params\":{\"${kw}\":\"${value}\"},\"model\":\"${modelName}\",\"active_features\":[\"${TemplateName}\"]}}}}}";
}
