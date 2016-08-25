package cn.daniel.myapp;

import cn.daniel.abridges.android.BridgeSFunctionCall;

/**
 * 请求JS接口协议类
 */
public interface BridgeInterface {
    /**
     * 获取JS端用户输入的用户信息【APP主动】
     * @return
     */
    BridgeSFunctionCall<User> getJsUserData();

    /**
     * 传递用户信息到JS端【APP主动】
     * @param user
     * @return
     */
    BridgeSFunctionCall<Void> setUserInfoFromApp(User user);
}
