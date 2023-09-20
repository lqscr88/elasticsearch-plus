package lq.simple.exception;

public class IndexException  extends RuntimeException{

    public final static String SETTINGS_ERROR_MESSAGE = "settings配置错误";
    public final static String MAPPINGS_ERROR_MESSAGE = "mappings配置错误";
    public final static String DSL_ERROR_MESSAGE = "DSL语句错误";
    public final static String INDEX_ERROR_MESSAGE = "index配置错误";
    public final static String INDEX_NAME_NULL_ERROR_MESSAGE = "索引名称不能为空";


    public IndexException(String message) {
        super(message);
    }
}
