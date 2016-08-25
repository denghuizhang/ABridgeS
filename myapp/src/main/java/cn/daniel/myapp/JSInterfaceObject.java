package cn.daniel.myapp;

import android.content.Context;
import android.widget.Toast;

import cn.daniel.abridges.js.BridgeMethod;

/**
 * JS接口调用类
 */
public class JSInterfaceObject {
    private Context context;

    public JSInterfaceObject(Context context) {
        this.context = context;
    }

    @BridgeMethod
    public User getUserInfoFromApp(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        return new User("san", "zhang", 15);
    }

    @BridgeMethod
    public void showToastMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
