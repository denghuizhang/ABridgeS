package cn.daniel.abridges.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import cn.daniel.abridges.ABridgeS;
import cn.daniel.abridges.utils.ABridgeSException;
import cn.daniel.abridges.utils.CommonUtils;

public class ScriptInterface {
    private ABridgeS scripto;
    private Object javaScriptInterface;
    private boolean annotationProtectionEnabled;/*是否注解*/

    public ScriptInterface(ABridgeS scripto, Object jsInterface) {
        this(scripto, jsInterface, new ScriptInterfaceConfig());
    }

    public ScriptInterface(ABridgeS scripto, Object jsInterface, ScriptInterfaceConfig config) {
        this.scripto = scripto;
        this.javaScriptInterface = jsInterface;
        annotationProtectionEnabled = config.isAnnotationProtectionEnabled();
    }

    /**
     * JS调APP,结果无需JS回调，对应JS中的【AbridgeS.call】
     *
     * @param methodName    被调的APP方法
     * @param jsonArgs      【JS端传递】该APP方法的params
     */
    @JavascriptInterface
    public void call(final String methodName, final String jsonArgs) {
        CommonUtils.runOnUi(new Runnable() {
            @Override
            public void run() {
                callOnUi(methodName, jsonArgs);
            }
        });
    }

    /**
     * JS调APP,APP返回的结果需JS回调，对应JS中的【AbridgeS.callWithCallback】
     *
     * @param methodName   被调的APP方法
     * @param jsonArgs     【JS端传递】该APP方法的params
     * @param callbackCode JS回调函数的key，用于JS端
     */
    @JavascriptInterface
    public void callWithCallback(final String methodName, final String jsonArgs, final String callbackCode) {
        CommonUtils.runOnUi(new Runnable() {
            @Override
            public void run() {
                Object response = callOnUi(methodName, jsonArgs);
                String callbackJsonString = "null";
                if(null != response){
                    callbackJsonString = scripto.getJavaScriptConverter().convertToString(response, response.getClass());
                }
                String responseCall = String.format("ABridgeS.removeCallBack('%s', '%s')",
                        callbackCode, callbackJsonString);
                scripto.getWebView().loadUrl("javascript:" + responseCall);
            }
        });
    }

    private Object callOnUi(String methodName, String jsonArgs) {
        JavaArguments args = new JavaArguments(jsonArgs);

        Method method = searchMethodByName(methodName, args);
        Object[] convertedArgs = convertArgs(args, method.getParameterTypes());

        try {
            if (hasSecureAnnotation(method)) {
                return method.invoke(javaScriptInterface, convertedArgs);
            } else if (!annotationProtectionEnabled) {
                return method.invoke(javaScriptInterface, convertedArgs);
            } else {
                Log.e("ABridgeS", "Method " + methodName + " not annotated with @BridgeMethod annotation");
            }
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ABridgeSException("Method call error", e);
        }
        return null;
    }

    /**
     * 参数集合
     * @param args              【JS数据】参数集合字符串
     * @param parameterTypes   【APP端】方法参数集合类型
     * @return                  APP参数集合
     */
    private Object[] convertArgs(JavaArguments args, Class<?>[] parameterTypes) {
        Object[] argsObjects = args.getArgs();
        Object[] convertedArgs = new Object[argsObjects.length];
        for (int i = 0; i < argsObjects.length; i++) {
            convertedArgs[i] = scripto.getJavaConverter()
                    .toObject(String.valueOf(argsObjects[i]), parameterTypes[i]);
        }
        return convertedArgs;
    }

    /**
     * 查找APP接口方法
     *
     * @param methodName JS调用的方法名
     * @param args       该方法参数
     * @return           APP方法
     */
    private Method searchMethodByName(String methodName, JavaArguments args) {
        ArrayList<Method> methodsForSearch = new ArrayList<>();
        Method[] declaredMethods = javaScriptInterface.getClass().getDeclaredMethods();

        /*查找相同的方法名*/
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(methodName)) {
                methodsForSearch.add(declaredMethod);
            }
        }

        /*查找相同参数个数的方法*/
        for (Iterator<Method> iterator = methodsForSearch.iterator(); iterator.hasNext(); ) {
            Method method = iterator.next();
            if (method.getParameterTypes().length != args.getArgs().length) {
                iterator.remove();
            }
        }

        //TODO .. 待完善 检验参数的类型

        if (methodsForSearch.size() == 1) {
            return methodsForSearch.get(0);
        } else if (methodsForSearch.size() > 1) {
            throw new ABridgeSException("Found more than one method");
        } else {
            throw new ABridgeSException(String.format("Method '%s' with arguments '%s' not found", methodName, args.getRaw()));
        }
    }

    /**
     * 是否为注解方法
     *
     * @param method    APP方法
     * @return          true:有注解
     */
    private boolean hasSecureAnnotation(Method method) {
        return method.isAnnotationPresent(BridgeMethod.class);
    }

}
