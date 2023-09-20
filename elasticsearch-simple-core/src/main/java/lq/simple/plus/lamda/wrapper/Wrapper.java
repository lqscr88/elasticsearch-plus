package lq.simple.plus.lamda.wrapper;
import java.util.Map;

public abstract class Wrapper<T>{

    public static final String BOOST = "B";


    /**
     * 实体对象（子类实现）
     *
     * @return 泛型 T
     */
    public abstract T getEntity();

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
