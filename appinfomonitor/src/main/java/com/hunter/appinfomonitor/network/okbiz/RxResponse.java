package com.hunter.appinfomonitor.network.okbiz;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Api Retrofit Net Response ..Observer Encapsulation.
 * Relation Order:
 * <pre>
 *       doTerminate -> doNext -> doComplete.
 *       doTerminate -> doOnError -> onError.
 *  正确只有一种.
 *  错误有几种：
 *      1，网络错误类(no NetWork) onNetworkError.
 *      2，服务器错误类 onServerError.
 *      3，业务错误类 onBizError.
 *      4，代码错误类
 *  onSuccess();
 *  onFailure()->onNetworkError();
 *             ->onServerError();
 *             ->onBizError();
 * </pre>
 * <p>
 * Created by hdhunter on 17-6-30.
 */
public abstract class RxResponse<T> implements Observer<T> {

    public RxResponse() {
    }

    /**
     * use unique tag,so we can cancel this RequestObservable.
     */
    public RxResponse(Object tag) {
    }

    public abstract void onSuccess(T result);

    public abstract void onFailure(Throwable e);


    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        } catch (Throwable e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            onFailure(e);
        } catch (Exception e1) {
            onFailure(e1);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
