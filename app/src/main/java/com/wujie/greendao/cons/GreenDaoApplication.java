package com.wujie.greendao.cons;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.*;

import java.util.List;

/**
 * Created by wujie on 2017/2/8.
 */
public class GreenDaoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
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
