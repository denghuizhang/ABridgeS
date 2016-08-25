package cn.daniel.abridges.js;

/**
 * 接口注解配置
 * js 调用 android 方法 （可有可无，加上注解好辨识）
 */
public class ScriptInterfaceConfig {

    private boolean annotationProtectionEnabled;

    public ScriptInterfaceConfig() {
        annotationProtectionEnabled = true;
    }

    public ScriptInterfaceConfig enableAnnotationProtection(boolean annotationProtectionEnabled) {
        this.annotationProtectionEnabled = annotationProtectionEnabled;
        return this;
    }

    public boolean isAnnotationProtectionEnabled() {
        return annotationProtectionEnabled;
    }

}
