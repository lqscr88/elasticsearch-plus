package lq.simple.plus.lamda.wrapper;

public class LambdaQueryWrapper<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapper<T>> {

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public LambdaQueryWrapper() {
        super.initNeed();
    }



}
