package cn.daniel.abridges;

/**
 * 加载H5页面时，一些准备工作的监听
 */
public interface ABridgeSPrepareListener {
    /**
     * 一些初始化内容（不需要就不实现）
     */
    void onBridgePrepared();
}
