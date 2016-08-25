package cn.daniel.abridges.android;

import java.lang.reflect.Method;

import cn.daniel.abridges.ABridgeS;

/**
 * 处理被调JS功能
 */
public class BridgeSFunction {

    private ABridgeS abridges;
    /*从当JS回调时的类别名，在代理类的构造函数里注册生成*/
    private final String proxyId;
    /*被调用的JS方法名+参数 function($param)*/
    private final String jsFunction;

    public BridgeSFunction(ABridgeS abridges, Method method, Object[] args, String proxyId) {
        this.abridges = abridges;
        this.proxyId = proxyId;
        this.jsFunction = buildJavaScriptFunctionCall(method, args);
    }

    /**
     * App调用JS的核心方法，组装被调的JS方法名和两个回调函数
     *
     * @param callCode key(哪个接口的回调，最后会根据这个值获取这个调用的接口)
     */
    public void callJavaScriptFunction(String callCode) {
        String jsCall = "javascript:"
                + "(function(){"
                + "	 try {"
                + "		var response = " + jsFunction
                + proxyId + ".onCallbackResponse('" + callCode + "', response);"
                + "	 } catch (err) {"
                + proxyId + ".onCallbackError('" + callCode + "', err.message);"
                + "	 }"
                + "})();";
        abridges.getWebView().loadUrl(jsCall);
    }

    /**
     * 组合js方法名+参数
     *
     * @param method
     * @param args
     * @return
     */
    private String buildJavaScriptFunctionCall(Method method, Object[] args) {
        JSArguments arguments = new JSArguments(abridges, args);
        String functionTemplate = "%s('%s');";
        return String.format(functionTemplate, method.getName(), arguments.getArguments());
    }
}
