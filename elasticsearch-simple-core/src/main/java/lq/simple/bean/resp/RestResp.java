//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lq.simple.bean.resp;




public class RestResp<T> {
    private ActionStatusMethod ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private T data;
    private Long count;

    public RestResp() {
        this.ActionStatus = ActionStatusMethod.OK;
        this.ErrorCode = 0;
        this.ErrorInfo = "";
    }

    private RestResp(Integer code, String msg) {
        this.ActionStatus = ActionStatusMethod.OK;
        this.ErrorCode = 0;
        this.ErrorInfo = "";
        this.ActionStatus = ActionStatusMethod.FAIL;
        this.ErrorCode = code;
        this.ErrorInfo = msg;
    }

    public RestResp(T data) {
        this.ActionStatus = ActionStatusMethod.OK;
        this.ErrorCode = 0;
        this.ErrorInfo = "";
        this.data = data;
    }

    public RestResp(T data, long count) {
        this(data);
        this.count = count;
    }


    public ActionStatusMethod getActionStatus() {
        return this.ActionStatus;
    }

    public void setActionStatus(ActionStatusMethod actionStatus) {
        this.ActionStatus = actionStatus;
    }


    public Integer getErrorCode() {
        return this.ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.ErrorCode = errorCode;
    }


    public String getErrorInfo() {
        return this.ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.ErrorInfo = errorInfo;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static <S> RestResp<S> error(Integer code, String msg) {
        return new RestResp(code, msg);
    }


    public static <S> RestResp<S> error(String msg) {
        return error(999999, msg);
    }


    public boolean isSuccess() {
        return this.getActionStatus() == ActionStatusMethod.OK;
    }


    public boolean isFailed() {
        return this.getActionStatus() == ActionStatusMethod.FAIL;
    }

    public static enum ActionStatusMethod {
        OK,
        FAIL;
        private ActionStatusMethod() {
        }
    }
}
