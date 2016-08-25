package cn.daniel.abridges.android;

import cn.daniel.abridges.utils.ABridgeSException;

public class CallBackException extends ABridgeSException {

    public CallBackException() {
    }

    public CallBackException(String detailMessage) {
        super(detailMessage);
    }


    public CallBackException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
