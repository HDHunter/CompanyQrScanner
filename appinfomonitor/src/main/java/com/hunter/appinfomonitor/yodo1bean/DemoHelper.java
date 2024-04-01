package com.hunter.appinfomonitor.yodo1bean;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.hunter.appinfomonitor.LogUtils;

/**
 * Created by caict on 2020/6/8.
 */

public class DemoHelper implements IIdentifierListener {

    private final AppIdsUpdater _listener;

    public DemoHelper(AppIdsUpdater callback) {
        _listener = callback;
    }

    public void getDeviceIds(Context cxt) {
        long timeb = System.currentTimeMillis();
        // 方法调用
        int nres = CallFromReflect(cxt);

        long timee = System.currentTimeMillis();
        long offset = timee - timeb;
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备
            LogUtils.e(getClass().getSimpleName(), "不支持的设备");
        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错
            LogUtils.e(getClass().getSimpleName(), "加载配置文件出错");
        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商
            LogUtils.e(getClass().getSimpleName(), "不支持的设备厂商");
        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
            LogUtils.e(getClass().getSimpleName(), "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程");
        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错
            LogUtils.e(getClass().getSimpleName(), "反射调用出错");
        }
        LogUtils.e(getClass().getSimpleName(), "return value: " + nres);
    }

    /*
     * 方法调用
     *
     * */
    private int CallFromReflect(Context cxt) {
        try {
            return MdidSdkHelper.InitSdk(cxt, true, this);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * 获取相应id
     *
     * */
    @Override
    public void OnSupport(boolean isSupport, IdSupplier supplier) {
        if (supplier == null) {
            return;
        }
        String oaid = supplier.getOAID();
        String vaid = supplier.getVAID();
        String aaid = supplier.getAAID();
        StringBuilder builder = new StringBuilder();
        builder.append("是否支持oaid: ").append(isSupport ? "true" : "false").append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext = builder.toString();
        if (_listener != null) {
            _listener.OnIdsAvalid(idstext);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull String ids);
    }

}
