package cn.daniel.abridges.utils;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.daniel.abridges.android.BridgeSFunctionCall;

public class CommonUtils {

    /**
     * 检查在android端定义的script interface接口
     * 这些script interface接口不能被继承
     * @param service 接口类
     * @param <T>
     */
    public static <T> void validateScriptInterface(Class<T> service) {
        /*判断是否为接口*/
        if (!service.isInterface()) {
            throw new IllegalArgumentException("调用的API类必须是interfaces");
        }
        /*解决JS注入漏洞，接口类不能被继承，迫使API接口必须注册*/
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("该接口类不能extend其他的interfaces");
        }
    }

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 判断是否为基本数据类型
     * @param type
     * @return
     */
    public static boolean isPrimitiveWrapper(Class<?> type) {
        return (Boolean.class == type)
                || (Byte.class == type)
                || (Character.class == type)
                || (Double.class == type)
                || (Float.class == type)
                || (Integer.class == type)
                || (Long.class == type)
                || (Short.class == type);
    }


    public static boolean hasNull(Object[] objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取被调方法(该方法为定义的srcript interface方法)的返回值类型
     * @param method
     * @return
     */
    public static Class<?> getCallResponseType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (!(method.getReturnType().isAssignableFrom(BridgeSFunctionCall.class))) {
            throw new IllegalArgumentException("被调方法的返回值类型必须类似为ScriptoFunctionCall<T>");
        }
        /*必须是参数化类型 例如 BridgeSFunctionCall<String>*/
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("被调方法的返回值类型必须类似为ScriptoFunctionCall<T>");
        }
        return (Class<?>) ((ParameterizedType) returnType).getActualTypeArguments()[0];
    }

    /**
     * UI主线程
     * @param task
     */
    public static void runOnUi(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }

}
