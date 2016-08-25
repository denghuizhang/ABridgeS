package cn.daniel.abridges.js;

import org.json.JSONArray;
import org.json.JSONException;

import cn.daniel.abridges.utils.CommonUtils;

public class JavaArguments {
    private String raw;
    /** JS端传递过来的参数json字符串 */
    private JSONArray jsonArgsArray;
    /** 参数类型集合 */
    private Class<?>[] argsTypes;
    /** 参数对象集合 */
    private Object[] argsObjects;
    /** 是否有null参数 */
    private boolean hasNullArg;

    public JavaArguments(String jsonArgs) {
        try {
            this.jsonArgsArray = new JSONArray(jsonArgs);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON arguments from JS!", e);
        }

        this.argsTypes = initArgsTypes();
        this.argsObjects = initArgs();
        this.hasNullArg = CommonUtils.hasNull(argsObjects);
    }

    public String getRaw() {
        return raw;
    }

    /**
     * 获取params类型
     *
     * @return
     */
    private Class<?>[] initArgsTypes() {
        Class<?>[] argsTypes = new Class<?>[jsonArgsArray.length()];
        for (int i = 0; i < jsonArgsArray.length(); i++) {
            try {
                argsTypes[i] = jsonArgsArray.get(i).getClass();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return argsTypes;
    }

    /**
     * 获取各个param
     *
     * @return
     */
    private Object[] initArgs() {
        Object[] args = new Object[jsonArgsArray.length()];
        for (int i = 0; i < jsonArgsArray.length(); i++) {
            try {
                if (jsonArgsArray.isNull(i)) {
                    args[i] = null;
                } else {
                    args[i] = jsonArgsArray.get(i);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return args;
    }

    public Object[] getArgs() {
        return argsObjects;
    }

    public Class<?>[] getArgsTypes() {
        return argsTypes;
    }

    public boolean hasNullArg() {
        return hasNullArg;
    }

}
