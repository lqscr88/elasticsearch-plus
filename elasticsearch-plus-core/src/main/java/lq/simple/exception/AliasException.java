package lq.simple.exception;

public class AliasException extends RuntimeException{

    public final static String ALIAS_ERROR_MESSAGE = "alias配置错误";

    public AliasException(String message) {
        super(message);
    }
}
