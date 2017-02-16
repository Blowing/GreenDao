package com.wujie.greendao.broadcast;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendao.cons.GreenDaoApplication;

import java.io.UnsupportedEncodingException;

/**
 * Created by wujie on 2017/2/8.
 */
public class HuaweiMessageReceiver extends PushReceiver{

    @Override
    public void onToken(Context context, String s, Bundle bundle) {
        String belongId = bundle.getString("belongId");
        String content = "get token and belongId successful, token = " +
                s + ", belongId = " + belongId;
        GreenDaoApplication.getApplication().HuaweiToken = s;
        Log.d(BaseActivity.TAG, content);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] bytes, Bundle bundle) {
        try {
            String content = "Receive a Push pass-by message: " + new String(bytes, "UTF-8" );
            Log.d(BaseActivity.TAG, content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
    }

    @Override
    public void onEvent(Context context, Event event, Bundle bundle) {
        Log.d(BaseActivity.TAG, "dedededededede" + event.name());
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = bundle.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            //String content = "receive extented notification message:" + bundle.getString(BOUND_KEY.pushMsgKey);
            GreenDaoApplication.getApplication().Extra = bundle.getString(BOUND_KEY.pushMsgKey);
            Log.d(BaseActivity.TAG, "dedededededede");
        }
        super.onEvent(context, event, bundle);
    }


    @Override
    public void onPushState(Context context, boolean b) {
        try {
            String content = "The current push status: " + (b ? "Connected" : "Disconnected");
            Log.d(BaseActivity.TAG, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
