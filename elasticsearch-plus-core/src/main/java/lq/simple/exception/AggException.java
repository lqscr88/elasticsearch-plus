package lq.simple.exception;

public class AggException extends RuntimeException{

    public final static String AGG_ERROR_MESSAGE = "Agg聚合参数不能为空";

    public AggException(String message) {
        super(message);
    }
}
