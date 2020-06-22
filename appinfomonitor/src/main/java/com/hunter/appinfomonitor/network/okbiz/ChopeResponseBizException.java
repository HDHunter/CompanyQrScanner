package com.hunter.appinfomonitor.network.okbiz;

/**
 * JSON Response,business Error.
 * <p>
 * Created by hdhunter on 17-7-18.
 */
public class ChopeResponseBizException extends Exception {

    public ChopeResponseBizException() {
        super();
    }

    public ChopeResponseBizException(String message) {
        super(message);
    }

    public ChopeResponseBizException(String message, Throwable cause) {
        super(message, cause);
    }
}
