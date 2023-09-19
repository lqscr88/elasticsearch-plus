package lq.simple.plus.lamda.wrapper;

import lq.simple.exception.LamdaException;
import lq.simple.plus.lamda.support.ColumnCache;
import lq.simple.util.LambdaUtils;
import lq.simple.util.StringUtils;
import java.lang.invoke.SerializedLambda;
import java.util.*;
import java.util.function.Function;

public class AbstractLambdaWrapper<T, Children extends AbstractLambdaWrapper<T, Children>>extends AbstractWrapper<T, Function<T, ?>, Children> {

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;


    protected String columnToString(Function<T, ?> column) {
        return columnToString(column, true);
    }

    protected String columnToString(Function<T, ?> column, boolean onlyColumn) {
        return getColumn(LambdaUtils.resolveByField(column), onlyColumn);
    }

    private String getColumn(SerializedLambda lambda, boolean onlyColumn) {
        String fieldName = StringUtils.resolveFieldName(lambda.getImplMethodName());
        if (!initColumnMap || !columnMap.containsKey(fieldName.toUpperCase(Locale.ENGLISH))) {
            String entityClassName = lambda.getImplClass().replace('/', '.');
            columnMap = LambdaUtils.getColumnMap(entityClassName);
            if (Objects.isNull(columnMap)){
                throw new LamdaException(LamdaException.AGG_ERROR_MESSAGE);
            }
            initColumnMap = true;
        }
        return Optional.ofNullable(columnMap.get(fieldName.toUpperCase(Locale.ENGLISH)))
                .map(onlyColumn ? ColumnCache::getColumn : ColumnCache::getColumnSelect)
                .orElseThrow(() -> new LamdaException(StringUtils.format("your property named \"%s\" cannot find the corresponding database column name!", fieldName)));

    }
}
