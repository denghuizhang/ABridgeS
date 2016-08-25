package cn.daniel.abridges.android;

import cn.daniel.abridges.ABridgeS;
import cn.daniel.abridges.converter.JavaScriptConverter;
import cn.daniel.abridges.utils.CommonUtils;

/**
 * JS接口参数的组装类
 * 依赖APP端协议接口的参数，生成JS端交互接口参数
 */
public class JSArguments {

    private ABridgeS scripto;
    private String[] stringArgs; /** 交互接口方法的参数(依据APP端产生)*/

    public JSArguments(ABridgeS scripto, Object[] argsObjects) {
        this.scripto = scripto;
        this.stringArgs = initArgs(argsObjects);
    }

    public String[] initArgs(Object[] argsObjects) {
        if (argsObjects == null) {
            return new String[0];
        }

        JavaScriptConverter javaScriptConverter = scripto.getJavaScriptConverter();
        String[] resultArgs = new String[argsObjects.length];

        for (int i = 0; i < argsObjects.length; i++) {
            Object argument = argsObjects[i];
            if (argument == null) {
                resultArgs[i] = "null";
            } else if (CommonUtils.isPrimitiveWrapper(argument.getClass()) || argument.getClass().isPrimitive()) {
                resultArgs[i] = String.valueOf(argument);
            } else if (argument.getClass() == String.class) {
                resultArgs[i] = String.format("'%s'", argument);
            } else {
                Object arg = argsObjects[i];
                resultArgs[i] = javaScriptConverter.convertToString(arg, arg.getClass());
            }
        }
        return resultArgs;
    }

    /**
     * 参数集合组合为参数字符串，参数之间用逗号分割
     * @return 组合好的参数字符串
     */
    public String getArguments() {
        String resultArgsString = "";
        for (int i = 0; i < stringArgs.length; i++) {
            resultArgsString += (i == 0) ? stringArgs[i] : "," + stringArgs[i];
        }
        return resultArgsString;
    }

    /**
     * 参数个数
     * @return
     */
    public int getArgumentsCount() {
        return stringArgs.length;
    }

}
