package lq.simple.exception;

public class SearchException extends RuntimeException{

    public final static String MATCH_ONE = "match方法只支持单字段查询";

    public SearchException(String message) {
        super(message);
    }
}
