#ABridgeS
Android Bridge JavaScript ，Android 桥接 JavaScript的一个框架。
##Compile
[ ![Download](https://api.bintray.com/packages/denghuizhang/maven/abridges/images/download.svg) ](https://bintray.com/denghuizhang/maven/abridges/_latestVersion)
```
compile 'com.github.abridges:abridges:1.0.0'
```
##如何使用
JS端首先在框架的exters文件夹下拷贝ABridgeS.js这个文件，并在html中引用。这个JS文件作为框架的一部分，在交互中起非常关键的作用。
```
<!--html bridge交互框架-->
<script src="ABridgeS.js"></script>
```
###Android调用JS方法

1.【APP端】编写调用接口协议类，比如以下代码
```
public interface BridgeInterface {
    BridgeSFunctionCall<User> getJsUserData();
    BridgeSFunctionCall<Void> setUserInfoFromApp(User user);
}
```
**注意** 协议接口的返回类型都为
```
BridgeSFunctionCall<T>
```
JS端被调用方法的方法名必须与协议中的方法名一致，如果协议方法中有参数，在交互的过程中，这些参数值都会转成json字串，并传递给则JS端。也就是说，协议方法有几个参数，JS方法就会收到几个json串。如果调用的JS方法有返回值，则这些返回值的类型必须是协议方法中T的json字符串。也就是说，JS返回值是一个json字符串，并且能转换为T这个对象。假如JS无返回值，则T为Void，并且这个Void必不可少。如果想得到JS端传递的原始json数据，则T为RawResponse。

2.【JS端】编写被调用的方法，方法名要与协议类方法名一致，比如以下代码
```
function getJsUserData() {
    var user = {};
    user['name'] = document.getElementById('name_field').value;
    return JSON.stringify(user);
}

function setUserInfoFromApp(userJson){
    document.getElementById('src_field').value = userJson;
    var user = JSON.parse(userJson);
    document.getElementById('name_field').value = user.name;
}
```

3.【APP端】注册协议类，并调用交互
```
webView = ...
ABridgeS aBridgeS = new ABridgeS.Builder(webView).build();
BridgeInterface bridgeInterface = aBridgeS.create(BridgeInterface.class);
//调用
bridgeInterface.getJsUserData().onResponse(new BridgeSResponseCallback<User>() {
    @Override
    public void onResponse(User user) {//调用成功，返回JS传递的数据
    }
}).onError(new BridgeSErrorCallback() {
    @Override
    public void onError(CallBackException e) {//调用异常
    }
}).call();
```
至此，Android APP调用JS方法交互完成。
###JS调用Android方法
1.【JS端】编写JS调用App方法的接口协议。
比如以下的AInterface.js文件，注意要在html中引用。
```
function AInterface() {}
AInterface.getUserInfoFromApp = function(userJson){
    ABridgeS.callWithCallback('Android', arguments);
};
AInterface.showToastMessage = function(msg) {
  ABridgeS.call('Android', arguments);
};
```
**注意** 在该协议中，其中的方法名最终是和APP端接口调用类中的方法名是一致的，如果接口方法需要APP端返回值，则应该使用 ABridgeS.callWithCallback('别名', arguments)，如果不需要，则使用 ABridgeS.call('别名', arguments)，注意和APP端商量好别名，此别名在APP端注册调用类的时候使用。

2.【APP端】创建JS接口调用类，JS接口会直接请求该类中的带**@BridgeMethod** 注解的并且 **同名** 的公有方法。
```
public class JSInterfaceObject {
    ...
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
```

3.【APP端】注册上一步创建好的JS接口调用类，并传入该类的别名，比如以下代码
```
webView = ...
aBridgeS = new ABridgeS.Builder(webView).build();
aBridgeS.addJsInterface("Android", new JSInterfaceObject(this));
```

4.【JS端】在JS方法直接使用，如果有callback的，JS回调方法中收到的是APP端返回值对象的json字符串。
```
function getUserInfo(){
    //调用APP接口获取用户信息
    AInterface.getUserInfoFromApp("获取UserInfo",function(userJson){
        document.getElementById('src_field').value = userJson;
        var user = JSON.parse(userJson);
    });
}
function showAPPToast () {
    //调用APP的Toast，JS主动调用
    AInterface.showToastMessage("JS被加载");
}
```
##结束
在最后说明下，在ABridgeS.js被页面加载的时候会主动调用APP端的onBridgePrepared方法，activity可以实现这个监听，做一些初始化操作。
```
aBridgeS.onPrepared(new ABridgeSPrepareListener() {
    @Override
    public void onBridgePrepared() {
        Log.e("ABridgeS", "onBridgePrepared");
    }
});
```

#License
Copyright 2016 Zhang.Daniel
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.