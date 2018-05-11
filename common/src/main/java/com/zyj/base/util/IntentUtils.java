package com.zyj.base.util;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.zyj.base.R;

import java.io.File;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import timber.log.Timber;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by lan on 2017-09-27.
 */

public class IntentUtils {
    public static final int WIFICIPHER_NOPASS = 0;
    public static final int WIFICIPHER_WEP = 1;
    public static final int WIFICIPHER_WPA = 2;

    /**
     * 获取系统软件包版本号
     */
    public static String getVersionName(Context context){
        PackageManager pkgManager = context.getPackageManager();

        try{
            PackageInfo info = pkgManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            return info.versionName;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取本地软件包版本号
     * archiveFilePath 为apk的路径
     */
    public static String getVersionName(Context context, String archiveFilePath){
        PackageManager pkgManager = context.getPackageManager();

        try{
            PackageInfo info = pkgManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);

            return info.versionName;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 安装apk
     */
    public static void installApk(Context context, File file) {
        if (file == null || !file.exists()) {
            return ;
        }
        Uri uri = Uri.fromFile(file);
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(localIntent);
    }
    /**
     * 获取拍照Uri
     */
    public static Uri getImageUri(Context context, File file) {
        String imgAuthority = context.getString(R.string.provider_image);
        Uri fileUri = FileProvider.getUriForFile(context, imgAuthority, file);

        return fileUri;
    }
    /**
     * 获取拍照
     */
    public static Intent getImageCapture(Uri fileUri) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        return openCameraIntent;
    }
    /**
     * 是否开启 wifi true：开启 false：关闭
     * @param isEnable
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public static void setWifi(Context context, boolean isEnable) {
        WifiManager mWm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //
        System.out.println("wifi===="+mWm.isWifiEnabled());
        if (isEnable) {// 开启wifi
            if (!mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(true);
            }
        } else {
            // 关闭 wifi
            if (mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(false);
            }
        }
    }
    /**
     * 连接指定wifi
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public static void connectWifi(Context context, String ssid, String password, int type) {
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //WifiManager的addNetwork接口，传入WifiConfiguration后，得到对应的NetworkId
        int netId = mWifiManager.addNetwork(createWifiConfig(ssid, password, type));
        //其中boolean参数，主要用于指定是否需要断开其它Wifi网络
        boolean enable = mWifiManager.enableNetwork(netId, true);
        Log.d("ZJTest", "enable: " + enable);
        //可选操作，让Wifi重新连接最近使用过的接入点
        //如果上文的enableNetwork成功，那么reconnect同样连接netId对应的网络
        //若失败，则连接之前成功过的网络
/*        boolean reconnect = mWifiManager.reconnect();
        Log.d("ZJTest", "reconnect: " + reconnect);*/
    }

    private static WifiConfiguration createWifiConfig(String ssid, String password, int type) {
        //初始化WifiConfiguration
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        //指定对应的SSID
        config.SSID = "\"" + ssid + "\"";

        //不需要密码的场景
        if(type == WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //以WEP加密的场景
        } else if(type == WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
            //以WPA加密的场景，自己测试时，发现热点以WPA2建立时，同样可以用这种配置连接
        } else if(type == WIFICIPHER_WPA) {
            config.preSharedKey = "\""+password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    /**
     * 得到 wifi信号值
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE})
    public static int getWifiRssi(Context context) {
        WifiManager mWm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWm.getConnectionInfo().getRssi();
    }
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static NetworkInfo getActiveNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            return connectivity.getActiveNetworkInfo();
        }
        return null;
    }
    /**
     * 检测当的网络IP
     * @param context Context
     * @return true 表示网络可用
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public static String getNetworkIP(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
            }
        }
        return "";
    }
    /**
     * 获取运营商信息
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static void getSimVersion(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        try{
            String networkOperatorName = telephonyManager.getNetworkOperatorName();
            String simOperatorName = telephonyManager.getSimOperatorName();
            String imei = telephonyManager.getImei();
            String line1Number = telephonyManager.getLine1Number();
            int networkType = telephonyManager.getNetworkType();
            int simState = telephonyManager.getSimState();

            Timber.e("networkOperatorName:%s, simOperatorName:%s, imei:%s, line1Number:%s," +
                            "networkType:%d, simState:%d", networkOperatorName, simOperatorName,
                    imei, line1Number, networkType, simState);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关机
     */
    public static void shutdown(Context context) {
        try {
            //获得ServiceManager类
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager,false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 电亮屏幕
     * 一定要加入权限： <uses-permission
     * android:name="android.permission.DISABLE_KEYGUARD"></uses-permission>
     */
    public static void wakeUpLock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();

        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
    }
    /**
     * 解锁
     * 一定要加入权限： <uses-permission
     * android:name="android.permission.DISABLE_KEYGUARD"></uses-permission>
     */
    @RequiresPermission(Manifest.permission.DISABLE_KEYGUARD)
    public static void unlock(Context context) {
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
