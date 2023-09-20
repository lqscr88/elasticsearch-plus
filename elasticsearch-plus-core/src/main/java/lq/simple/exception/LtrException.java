package lq.simple.exception;

public class LtrException extends RuntimeException{

    public final static String PARAMS_ERROR_MESSAGE = "params参数不能为空";

    public final static String MODEL_NAME_ERROR_MESSAGE = "modelName参数不能为空";

    public final static String TEMPLATE_NAMES_ERROR_MESSAGE = "templateNames参数不能为空";
    public LtrException(String message) {
        super(message);
    }
}
