package cn.daniel.abridges.converter;

import cn.daniel.abridges.utils.ABridgeSException;

/**
 * json转换异常
 */
public class ConverterException extends ABridgeSException {
    public ConverterException() {
    }

    public ConverterException(String detailMessage) {
        super(detailMessage);
    }

    public ConverterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
