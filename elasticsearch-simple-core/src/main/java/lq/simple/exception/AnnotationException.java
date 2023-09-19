package lq.simple.exception;

public class AnnotationException extends RuntimeException{

    public final static String NON_CACHE = "cannot find column's cache";


    public AnnotationException(String message) {
        super(message);
    }
}
