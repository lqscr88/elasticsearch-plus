package lq.simple.plus.lamda.wrapper;

import lq.simple.exception.LamdaException;
import lq.simple.plus.lamda.support.ColumnCache;
import lq.simple.plus.lamda.support.SFunction;
import lq.simple.plus.lamda.support.SerializedLambda;
import lq.simple.util.LambdaUtils;
import lq.simple.util.StringUtils;
import java.util.*;

public class AbstractLambdaWrapper<T, Children extends AbstractLambdaWrapper<T, Children>>extends AbstractWrapper<T, SFunction<T, ?>, Children> {

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;

    @Override
    protected String columnToString(SFunction<T, ?> column) {
        return columnToString(column, true);
    }

    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        return getColumn(LambdaUtils.resolve(column), onlyColumn);
    }

    private String getColumn(SerializedLambda lambda, boolean onlyColumn) {
        String fieldName = StringUtils.resolveFieldName(lambda.getImplMethodName());
        if (!initColumnMap || !columnMap.containsKey(fieldName.toUpperCase(Locale.ENGLISH))) {
            String entityClassName = lambda.getImplClassName();
            columnMap = LambdaUtils.getColumnMap(entityClassName);
            if (Objects.isNull(columnMap)){
                throw new LamdaException(StringUtils.format("cannot find column's cache for \"%s\", so you cannot used \"%s\"!",
                        entityClassName, typedThis.getClass()));
            }
            initColumnMap = true;
        }
        return Optional.ofNullable(columnMap.get(fieldName.toUpperCase(Locale.ENGLISH)))
                .map(onlyColumn ? ColumnCache::getColumn : ColumnCache::getColumnSelect)
                .orElseThrow(() -> new LamdaException(StringUtils.format("your property named \"%s\" cannot find the corresponding database column name!", fieldName)));

    }
}
