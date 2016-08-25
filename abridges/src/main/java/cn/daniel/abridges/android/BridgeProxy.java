package cn.daniel.abridges.android;


import android.webkit.JavascriptInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import cn.daniel.abridges.ABridgeS;
import cn.daniel.abridges.utils.CommonUtils;
import cn.daniel.abridges.utils.StringUtils;

public class BridgeProxy implements InvocationHandler {

    private ABridgeS abridges;
    private String proxyId;     /*从当回调接口的类别名*/
    private HashMap<String, BridgeSFunctionCall> functionCalls;

    public BridgeProxy(ABridgeS abridges) {
        this.abridges = abridges;
        functionCalls = new HashMap();
        proxyId = StringUtils.randomString(5); /*20位随机字符串*/
        /*此处添加了JS接口，用于接口的回调【APP调用JS接口后的回调】*/
        abridges.getWebView().addJavascriptInterface(this, proxyId);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        BridgeSFunction scriptoFunction = new BridgeSFunction(abridges, method, args, proxyId);
        Class<?> returnType = CommonUtils.getCallResponseType(method);
        String callCode = StringUtils.randomStringNumeric(5);
        BridgeSFunctionCall scriptoFunctionCall = new BridgeSFunctionCall(scriptoFunction, returnType, callCode);
        functionCalls.put(callCode, scriptoFunctionCall);
        return scriptoFunctionCall;
    }

    /**
     * 【APP调JS】调用JS接口成功的回调
     *
     * @param callbackCode   回调的键值，在hashMap中确定那个APP接口的回调
     * @param responseString JS接口返回的json数据
     */
    @JavascriptInterface
    public void onCallbackResponse(final String callbackCode, final String responseString) {
        CommonUtils.runOnUi(new Runnable() {
            @Override
            public void run() {
                onCallbackResponseUi(callbackCode, responseString);
            }
        });
    }

    /**
     * 【APP调JS】调用JS接口失败的回调
     *
     * @param callbackCode 回调的键值，在hashMap中确定那个APP接口的回调
     * @param message      错误信息
     */
    @JavascriptInterface
    public void onCallbackError(final String callbackCode, final String message) {
        CommonUtils.runOnUi(new Runnable() {
            @Override
            public void run() {
                onCallbackErrorUi(callbackCode, message);
            }
        });
    }

    private void onCallbackResponseUi(String callbackCode, String responseString) {
        BridgeSFunctionCall functionCall = functionCalls.remove(callbackCode);
        BridgeSResponseCallback callback = functionCall.getResponseCallback();

        if (callback == null) {
            return;
        }

        /*此处的返回值类型是APP接口的返回值类型,既定义接口时ScriptoFunctionCall<T>中的T*/
        Class<?> responseType = functionCall.getResponseType();
        if (responseString == null || responseType.isAssignableFrom(Void.class)) {
            /*如果T为Void或者JS端接口返回null*/
            callback.onResponse(null);
        } else if (responseType.isAssignableFrom(RawResponse.class)) {
            /*如果T类型为RawResponse，则直接返回该JS端接口返回的值(应该是一个json字符串)*/
            callback.onResponse(new RawResponse(responseString));
        } else {
            /*直接转换为T对象 */
            Object response = abridges.getJavaConverter().toObject(responseString, responseType);
            callback.onResponse(response);
        }
    }

    private void onCallbackErrorUi(String callbackCode, String message) {
        BridgeSFunctionCall functionCall = functionCalls.remove(callbackCode);
        BridgeSErrorCallback callback = functionCall.getErrorCallback();

        /*失败监听为null时，直接抛出异常 APPh会crash，release版本不能设置throwOnError(true)*/
        if (callback == null && functionCall.isThrowOnError()) {
            throw new CallBackException(message);
        } else if (callback != null) {
            callback.onError(new CallBackException(message));
        }
    }

}
