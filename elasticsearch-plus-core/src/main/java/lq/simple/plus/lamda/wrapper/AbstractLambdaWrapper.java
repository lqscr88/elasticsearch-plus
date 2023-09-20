package lq.simple.plus.lamda.wrapper;

import lq.simple.plus.lamda.support.SFunction;
import lq.simple.plus.lamda.support.SerializedLambda;
import lq.simple.util.LambdaUtils;
import lq.simple.util.StringUtils;

import java.util.*;

@SuppressWarnings("serial")
public abstract class AbstractLambdaWrapper<T, Children extends AbstractLambdaWrapper<T, Children>>
        extends AbstractWrapper<T, SFunction<T, ?>, Children> {

    @Override
    protected String columnToString(SFunction<T, ?> column) {
        return getColumn(LambdaUtils.resolve(column));
    }


    private String getColumn(SerializedLambda lambda) {
        return StringUtils.resolveFieldName(lambda.getImplMethodName()).toLowerCase(Locale.ENGLISH);
    }
}
