package com.hunter.appinfomonitor.network.okbiz;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * common compose.
 * <p>
 * Created by hdhunter on 17-6-26.
 */
public class RxResultHelper {


    private RxResultHelper() {

    }

    /**
     * 目前网络层不做任何业务处理。定义类型方便convert.
     * Hunter
     */
    public static <T> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>() {

            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
