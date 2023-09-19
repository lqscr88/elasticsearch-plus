package lq.simple.plus.lamda.wrapper;

import lombok.Builder;
import lombok.val;
import lq.simple.exception.LamdaException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AbstractWrapper<T, R, Children extends AbstractWrapper<T, R, Children>> extends Wrapper<T> implements Compare<Children, R> {


    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;

    /**
     * 度量
     */
    protected Map<String, Object> paramNameValuePairs;
    private Integer from;
    private Integer size;
    private String analyzer;
    private String index;

    @Override
    public Children match(boolean condition, R column, Object val, Float boost) {
        return doIt(condition, columnToString(column), val, boost);
    }


    @Override
    public Children from(Integer val) {
        from = val;
        return typedThis;
    }

    @Override
    public Children size(Integer val) {
        size = val;
        return typedThis;
    }

    @Override
    public Children analyzer(String val) {
        analyzer = val;
        return typedThis;
    }

    @Override
    public Children index(String val) {
        index = val;
        return typedThis;
    }

    @Override
    public String getIndex() {
        return index;
    }

    /**
     * 对sql片段进行组装
     *
     * @param condition 是否执行
     * @return children
     */
    protected Children doIt(boolean condition, String column, Object val, Float boost) {
        if (!condition) {
            return typedThis;
        }
        if (Objects.isNull(val)) {
            return typedThis;
        }
        if (boost != 1f) {
            column = column + BOOST + boost;
        }
        paramNameValuePairs.put(column, val);
        return typedThis;
    }

    /**
     * 获取 columnName
     */
    protected String columnToString(R column) {
        if (column instanceof String) {
            return (String) column;
        }
        throw new LamdaException("not support this column !");
    }

    public Map<String, Object> getParamNameValuePairs() {
        return paramNameValuePairs;
    }


    /**
     * 必要的初始化
     */
    protected final void initNeed() {
        paramNameValuePairs = new HashMap<>(16);
        from = 0;
        size = 10;
    }


    public String getAnalyzer() {
        return analyzer;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getFrom() {
        return from;
    }
}
