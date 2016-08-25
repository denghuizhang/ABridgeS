package cn.daniel.abridges;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.lang.reflect.Proxy;

import cn.daniel.abridges.converter.JavaConverter;
import cn.daniel.abridges.converter.JavaScriptConverter;
import cn.daniel.abridges.android.BridgeProxy;
import cn.daniel.abridges.js.ScriptInterface;
import cn.daniel.abridges.js.ScriptInterfaceConfig;
import cn.daniel.abridges.utils.CommonUtils;

public class ABridgeS {
    private WebView webView;
    private JavaScriptConverter javaScriptConverter;
    private JavaConverter javaConverter;

    private ABridgeSPrepareListener prepareListener;

    private ABridgeS(Builder builder) {
        this.webView = builder.webView;
        this.javaScriptConverter = builder.javaScriptConverter;
        this.javaConverter = builder.javaConverter;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new ABridgeSPrepareInterface(), "ABridgeSPrepare");
    }

    private class ABridgeSPrepareInterface {
        /**
         * 该接口在加载JS文件时调用（第一个被调用的接口）
         */
        @JavascriptInterface
        public void onBridgePrepared() {
            CommonUtils.runOnUi(new Runnable() {
                @Override
                public void run() {
                    if (prepareListener != null) {
                        prepareListener.onBridgePrepared();
                    }
                }
            });
        }
    }

    /**
     * 注册被调用的JS接口协议类
     *
     * @param script 协议类
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> script) {
        CommonUtils.checkNotNull(script, "Script class can not be null");
        CommonUtils.validateScriptInterface(script);
        return (T) Proxy.newProxyInstance(script.getClassLoader(),
                new Class<?>[]{script}, new BridgeProxy(this));
    }

    public WebView getWebView() {
        return webView;
    }

    public JavaScriptConverter getJavaScriptConverter() {
        return javaScriptConverter;
    }

    public JavaConverter getJavaConverter() {
        return javaConverter;
    }

    /**
     * 添加JS调用APP的接口类
     *
     * @param tag 类别名
     * @param obj 类对象
     */
    public void addJsInterface(String tag, Object obj) {
        webView.addJavascriptInterface(new ScriptInterface(this, obj), tag);
    }

    /**
     * 添加JS调用APP的接口类
     *
     * @param tag    类别名
     * @param obj    类对象
     * @param config 注解配置
     */
    public void addJsInterface(String tag, Object obj, ScriptInterfaceConfig config) {
        webView.addJavascriptInterface(new ScriptInterface(this, obj, config), tag);
    }

    public void removeInterface(String tag) {
        webView.removeJavascriptInterface(tag);
    }

    /**
     * @param prepareListener
     */
    public void onPrepared(ABridgeSPrepareListener prepareListener) {
        this.prepareListener = prepareListener;
    }

    public static class Builder {
        private WebView webView;
        private JavaScriptConverter javaScriptConverter;
        private JavaConverter javaConverter;

        public Builder(WebView webView) {
            this.webView = webView;
            javaScriptConverter = new JavaScriptConverter();
            javaConverter = new JavaConverter();
        }

        public Builder setJavaScriptConverter(JavaScriptConverter javaScriptConverter) {
            CommonUtils.checkNotNull(javaScriptConverter, "Converter can not be null");
            this.javaScriptConverter = javaScriptConverter;
            return this;
        }

        public Builder setJavaConverter(JavaConverter javaConverter) {
            CommonUtils.checkNotNull(javaConverter, "Converter can not be null");

            this.javaConverter = javaConverter;
            return this;
        }

        public ABridgeS build() {
            return new ABridgeS(this);
        }
    }

}