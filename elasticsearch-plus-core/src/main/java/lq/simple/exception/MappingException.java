package lq.simple.exception;

public class MappingException extends RuntimeException{

    public final static String MAPPING_ERROR_MESSAGE = "mapping配置错误";

    public MappingException(String message) {
        super(message);
    }
}
