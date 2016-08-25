package cn.daniel.abridges.utils;

public class ABridgeSException extends RuntimeException {
    public ABridgeSException() {

    }

    public ABridgeSException(String detailMessage) {
        super(detailMessage);
    }

    public ABridgeSException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
