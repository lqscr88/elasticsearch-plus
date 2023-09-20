package lq.simple.exception;

public class UpdateException extends RuntimeException{

    public final static String UPDATE_ERROR_MESSAGE = "id不能为空";


    public UpdateException(String message) {
        super(message);
    }
}
