package lq.simple.exception;

public class LamdaException extends RuntimeException{

    public final static String AGG_ERROR_MESSAGE = "Agg聚合参数不能为空";

    public LamdaException(String message) {
        super(message);
    }
}
