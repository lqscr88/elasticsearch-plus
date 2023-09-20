package lq.simple.exception;

public class HighlightException extends RuntimeException{

    public final static String HIGHLIGHT_ERROR_MESSAGE = "Highlight高亮合参数不能为空";

    public HighlightException(String message) {
        super(message);
    }
}
