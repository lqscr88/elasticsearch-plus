package lq.simple.plus.lamda.wrapper;

import lq.simple.plus.annotation.IndexName;
import lq.simple.util.GenericSuperclassUtils;

@SuppressWarnings("serial")
public class LambdaQueryWrapper<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapper<T>> {

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public LambdaQueryWrapper() {
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public LambdaQueryWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }


    /**
     * 获取 index
     */
    protected void genericSuperclassIndexAnnotation() {
        if (entityClass.isAnnotationPresent(IndexName.class)) {
            String value = entityClass.getAnnotation(IndexName.class).value();
            setIndex(value);
        }
    }



}
