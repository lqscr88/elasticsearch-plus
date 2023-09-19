package lq.simple.plus.lamda.wrapper;
import java.util.Map;

public abstract class Wrapper<T>{

    public static final String BOOST = "B";

    public abstract Map<String, Object> getParamNameValuePairs();

    /**
     * 起始页
     */
    public abstract Integer getFrom();




    /**
     * 总大小
     */
    public abstract Integer  getSize();


    /**
     * 分词器
     */
    public abstract String getAnalyzer();


    /**
     * 索引
     */
    public abstract String getIndex();

}
