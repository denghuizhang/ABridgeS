package cn.daniel.abridges.android;

/**
 * 【回调类型】原始数据返回类型，JS接口返回的原始数据
 */
public class RawResponse {

    private String response;

    public RawResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

}
