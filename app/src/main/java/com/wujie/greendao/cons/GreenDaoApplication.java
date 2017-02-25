package com.wujie.greendao.cons;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wujie.greendao.util.WifiClient;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujie on 2017/2/8.
 */
public class GreenDaoApplication extends MultiDexApplication {

    private static GreenDaoApplication mInstance;

    public List<String> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<String> albumList) {
        this.albumList = albumList;
    }

    private List<String> albumList = new ArrayList<>();

    public String HuaweiToken = "";

    public String Extra = "";

    public static GreenDaoApplication getApplication() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        NoHttp.initialize(this, new NoHttp.Config().setCacheStore(new DBCacheStore(this).setEnable(false))
                .setCookieStore(new DBCookieStore(this).setEnable(false))
                .setNetworkExecutor(new OkHttpNetworkExecutor()));
        Logger.setDebug(true);
        Logger.setTag("nohttp");
        mInstance = this;
        if(shouldInit()) {
            MiPushClient.registerPush(this,Constants.APP_ID, Constants.APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String s) {

            }

            @Override
            public void log(String s) {
                Log.d(Constants.TAG, s);
            }

            @Override
            public void log(String s, Throwable throwable) {
                Log.d(Constants.TAG, s, throwable);
            }
        };
        Constants.USB_IP = "http://" + WifiClient.getGayWay(this) + ":80" ;

    }

    private boolean shouldInit() {

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainPrcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info: processInfos
             ) {
            if( info.pid == myPid && mainPrcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
