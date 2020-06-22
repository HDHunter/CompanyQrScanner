package com.hunter.appinfomonitor.network.okbiz;

/**
 * empty implement class.
 * <p>
 * Created by hdhunter on 17-11-13.
 */
public class RxDefaultResponse<T> extends RxResponse<T> {
    @Override
    public void onSuccess(T result) {
        // 子类实现
    }

    @Override
    public void onFailure(Throwable e) {
        // 子类实现
    }
}
