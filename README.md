#ABridgeS
Android Bridge JavaScript ��Android �Ž� JavaScript��һ����ܡ�
##Compile
[ ![Download](https://api.bintray.com/packages/denghuizhang/maven/abridges/images/download.svg) ](https://bintray.com/denghuizhang/maven/abridges/_latestVersion)
```
compile 'com.github.abridges:abridges:1.0.0'
```
##���ʹ��
JS�������ڿ�ܵ�exters�ļ����¿���ABridgeS.js����ļ�������html�����á����JS�ļ���Ϊ��ܵ�һ���֣��ڽ�������ǳ��ؼ������á�
```
<!--html bridge�������-->
<script src="ABridgeS.js"></script>
```
###Android����JS����

1.��APP�ˡ���д���ýӿ�Э���࣬�������´���
```
public interface BridgeInterface {
    BridgeSFunctionCall<User> getJsUserData();
    BridgeSFunctionCall<Void> setUserInfoFromApp(User user);
}
```
**ע��** Э��ӿڵķ������Ͷ�Ϊ
```
BridgeSFunctionCall<T>
```
JS�˱����÷����ķ�����������Э���еķ�����һ�£����Э�鷽�����в������ڽ����Ĺ����У���Щ����ֵ����ת��json�ִ��������ݸ���JS�ˡ�Ҳ����˵��Э�鷽���м���������JS�����ͻ��յ�����json����������õ�JS�����з���ֵ������Щ����ֵ�����ͱ�����Э�鷽����T��json�ַ�����Ҳ����˵��JS����ֵ��һ��json�ַ�����������ת��ΪT������󡣼���JS�޷���ֵ����TΪVoid���������Void�ز����١������õ�JS�˴��ݵ�ԭʼjson���ݣ���TΪRawResponse��

2.��JS�ˡ���д�����õķ�����������Ҫ��Э���෽����һ�£��������´���
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

3.��APP�ˡ�ע��Э���࣬�����ý���
```
webView = ...
ABridgeS aBridgeS = new ABridgeS.Builder(webView).build();
BridgeInterface bridgeInterface = aBridgeS.create(BridgeInterface.class);
//����
bridgeInterface.getJsUserData().onResponse(new BridgeSResponseCallback<User>() {
    @Override
    public void onResponse(User user) {//���óɹ�������JS���ݵ�����
    }
}).onError(new BridgeSErrorCallback() {
    @Override
    public void onError(CallBackException e) {//�����쳣
    }
}).call();
```
���ˣ�Android APP����JS����������ɡ�
###JS����Android����
1.��JS�ˡ���дJS����App�����Ľӿ�Э�顣
�������µ�AInterface.js�ļ���ע��Ҫ��html�����á�
```
function AInterface() {}
AInterface.getUserInfoFromApp = function(userJson){
    ABridgeS.callWithCallback('Android', arguments);
};
AInterface.showToastMessage = function(msg) {
  ABridgeS.call('Android', arguments);
};
```
**ע��** �ڸ�Э���У����еķ����������Ǻ�APP�˽ӿڵ������еķ�������һ�µģ�����ӿڷ�����ҪAPP�˷���ֵ����Ӧ��ʹ�� ABridgeS.callWithCallback('����', arguments)���������Ҫ����ʹ�� ABridgeS.call('����', arguments)��ע���APP�������ñ������˱�����APP��ע��������ʱ��ʹ�á�

2.��APP�ˡ�����JS�ӿڵ����࣬JS�ӿڻ�ֱ����������еĴ�**@BridgeMethod** ע��Ĳ��� **ͬ��** �Ĺ��з�����
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

3.��APP�ˡ�ע����һ�������õ�JS�ӿڵ����࣬���������ı������������´���
```
webView = ...
aBridgeS = new ABridgeS.Builder(webView).build();
aBridgeS.addJsInterface("Android", new JSInterfaceObject(this));
```

4.��JS�ˡ���JS����ֱ��ʹ�ã������callback�ģ�JS�ص��������յ�����APP�˷���ֵ�����json�ַ�����
```
function getUserInfo(){
    //����APP�ӿڻ�ȡ�û���Ϣ
    AInterface.getUserInfoFromApp("��ȡUserInfo",function(userJson){
        document.getElementById('src_field').value = userJson;
        var user = JSON.parse(userJson);
    });
}
function showAPPToast () {
    //����APP��Toast��JS��������
    AInterface.showToastMessage("JS������");
}
```
##����
�����˵���£���ABridgeS.js��ҳ����ص�ʱ�����������APP�˵�onBridgePrepared������activity����ʵ�������������һЩ��ʼ��������
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