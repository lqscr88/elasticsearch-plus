package lq.simple.plus.exception;

public class AnnotationException extends RuntimeException{

    public final static String UPDATE_ERROR_MESSAGE = "id不能为空";


    public AnnotationException(String message) {
        super(message);
    }
}
