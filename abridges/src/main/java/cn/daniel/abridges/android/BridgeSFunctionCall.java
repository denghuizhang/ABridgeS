package cn.daniel.abridges.android;

public class BridgeSFunctionCall<T> {

    private BridgeSFunction scriptoFunction;
    private String callCode;

    private BridgeSResponseCallback<T> responseCallback;
    private BridgeSErrorCallback errorCallback;
    private Class<?> responseType;  /** T类型 */

    private boolean throwOnEror;    /** 是否抛出异常 */

    public BridgeSFunctionCall(BridgeSFunction scriptoFunction, Class<?> responseType, String callCode) {
        this.scriptoFunction = scriptoFunction;
        this.responseType = responseType;
        this.callCode = callCode;
        throwOnEror = false;
    }

    /**
     * 回调成功监听
     * @param responseCallback 回调监听
     * @return
     */
    public BridgeSFunctionCall<T> onResponse(BridgeSResponseCallback<T> responseCallback) {
        this.responseCallback = responseCallback;
        return this;
    }

    /**
     * 回调失败监听
     * @param errorCallback 失败监听
     * @return
     */
    public BridgeSFunctionCall<T> onError(BridgeSErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }

    public BridgeSResponseCallback<T> getResponseCallback() {
        return responseCallback;
    }

    public BridgeSErrorCallback getErrorCallback() {
        return errorCallback;
    }

    public BridgeSFunctionCall<T> throwOnError(boolean throwOnEror) {
        this.throwOnEror = throwOnEror;
        return this;
    }

    public boolean isThrowOnError() {
        return throwOnEror;
    }

    protected Class<?> getResponseType() {
        return responseType;
    }

    /**
     * APP调用JS的入口函数
     */
    public void call() {
        scriptoFunction.callJavaScriptFunction(callCode);
    }

}
