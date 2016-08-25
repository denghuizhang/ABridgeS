package cn.daniel.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import cn.daniel.abridges.ABridgeS;
import cn.daniel.abridges.ABridgeSPrepareListener;
import cn.daniel.abridges.android.BridgeSErrorCallback;
import cn.daniel.abridges.android.BridgeSResponseCallback;
import cn.daniel.abridges.android.CallBackException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WebView webView;
    Button btn1;
    Button btn2;

    ABridgeS aBridgeS;
    BridgeInterface bridgeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webView);

        aBridgeS = new ABridgeS.Builder(webView).build();
        bridgeInterface = aBridgeS.create(BridgeInterface.class);
        aBridgeS.addJsInterface("Android", new JSInterfaceObject(this));

        webView.loadUrl("file:///android_asset/index.html");
        aBridgeS.onPrepared(new ABridgeSPrepareListener() {
            @Override
            public void onBridgePrepared() {
                Log.e("ABridgeS", "onBridgePrepared");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: /*获取JS数据*/
                bridgeInterface.getJsUserData()
                        .onResponse(new BridgeSResponseCallback<User>() {
                            @Override
                            public void onResponse(User user) {
                                Toast.makeText(getApplicationContext(), "获取到的JS数据为" + user.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onError(new BridgeSErrorCallback() {
                            @Override
                            public void onError(CallBackException e) {
                                Log.e("ABridgeS", "调用JS接口异常：" + e.getMessage());
                            }
                        })
                        .call();
                break;
            case R.id.btn2:/*传输JS数据*/
                bridgeInterface.setUserInfoFromApp(new User("wu", "wang", 21))
                        .onError(new BridgeSErrorCallback() {
                            @Override
                            public void onError(CallBackException e) {
                                Log.e("ABridgeS", "传输数据失败" + e.getMessage());
                            }
                        })
                        .onResponse(new BridgeSResponseCallback<Void>() {
                            @Override
                            public void onResponse(Void aVoid) {
                                Log.e("ABridgeS", "传输数据成功");
                            }
                        })
                        .call();
                break;
        }
    }
}
