package com.hunter.appinfomonitor.yodo1bean;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.Locale;

public class PhoneUtil {

    public static String getUserAgent() {
        try {
            return getPhoneType();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机IMEI码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String imei = null;
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
                imei = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(imei) || "0".equals(imei) || "000000000000000".equals(imei)) {
            imei = "";
        }
        return imei;
    }

    /**
     * 获取手机IMSI码
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();
            if (imsi == null)
                imsi = "";
            return imsi;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机网络型号
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getNetworkOperatorName();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机机型:i9250
     *
     * @return
     */
    public static String getPhoneType() {
        try {
            String type = Build.MODEL;
            if (type != null) {
                type = type.replace(" ", "");
            }
            return type.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDevice() {
        try {
            return Build.DEVICE;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getProduct() {
        try {
            return Build.PRODUCT;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getType() {
        try {
            return Build.TYPE;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机操作系统版本名：如2.3.1
     *
     * @return
     */
    public static String getSDKVersionName() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机操作系统版本号：如4
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        try {
            return Build.VERSION.SDK;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机操作系统版本号：如4
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取手机号码
     *
     * @param context
     * @return
     */
    public static String getNativePhoneNumber(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String nativePhoneNumber = telephonyManager.getLine1Number();
            if (nativePhoneNumber == null) {
                nativePhoneNumber = "";
            }
            return nativePhoneNumber;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取屏幕尺寸，如:320x480
     *
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels + "x" + dm.heightPixels;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getDisplayDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 获取手机语言
     *
     * @return
     */
    public static String getPhoneLanguage() {
        try {
            String language = Locale.getDefault().getLanguage();
            if (language == null) {
                language = "";
            }
            return language;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机MAC地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info == null)
                return "";
            return info.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取基带版本
     *
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public static String getBaseand() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            return result.toString();
        } catch (Exception e) {
        }
        return "";
    }

    public static int getCacheSize(Context context) {
        try {
            return 1024 * 1024 * ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass() / 8;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取当前分辨率下指定单位对应的像素大小（根据设备信息） px,dip,sp -> px
     *
     * @param context
     * @param unit
     * @param size
     * @return
     */
    public static int getRawSize(Context context, int unit, float size) {
        try {
            Resources resources;
            if (context == null) {
                resources = Resources.getSystem();
            } else {
                resources = context.getResources();
            }
            return (int) TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断当前手机是否处于锁屏状态
     */
    public static boolean isLock(Context context) {
        try {
            KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return mKeyguardManager.inKeyguardRestrictedInputMode();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断当前手机是否处横屏状态
     */
    public static boolean isScreenChange(Context context) {
        try {
            Configuration mConfiguration = context.getResources().getConfiguration(); // 获取设置的配置信息
            int ori = mConfiguration.orientation; // 获取屏幕方向

            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {

                // 横屏
                return true;
            } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

                // 竖屏
                return false;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取手机ip地址
     */
    public static String getPhoneIp(Context context) {
        try {
            // 获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return intToIp(ipAddress);
        } catch (Exception e) {
            return "";
        }
    }

    private static String intToIp(int i) {
        try {
            return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        } catch (Exception e) {
            return "";
        }
    }
}
