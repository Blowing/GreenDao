package com.wujie.greendao.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.wujie.greendao.activity.NotificationActivity;
import com.wujie.greendao.cons.GreenDaoApplication;
import com.wujie.greendao.util.HistoryAccount;
import com.wujie.greendao.util.StorageUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by wujie on 2017/2/8.
 */
public class XiaomiMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        mMessage = miPushMessage.getContent();
        GreenDaoApplication.getApplication().Extra = mMessage;
        Log.d("PassThroughMessage", mMessage);
        if (!TextUtils.isEmpty(miPushMessage.getTopic())) {
            mTopic = miPushMessage.getTopic();
        } else if (!TextUtils.isEmpty(miPushMessage.getAlias())) {
            mAlias = miPushMessage.getAlias();
        } else if (!TextUtils.isEmpty(miPushMessage.getUserAccount())) {
            mUserAccount = miPushMessage.getUserAccount();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        mMessage = miPushMessage.getContent();
        GreenDaoApplication.getApplication().Extra = mMessage;
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.d("MessageClicked", mMessage);
        SharedPreferences sharedPreferences =context.getSharedPreferences("Extra", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("extra", mMessage);
        editor.apply();
        if (!TextUtils.isEmpty(miPushMessage.getTopic())) {
            mTopic = miPushMessage.getTopic();
        } else if (!TextUtils.isEmpty(miPushMessage.getAlias())) {
            mAlias = miPushMessage.getAlias();
        } else if (!TextUtils.isEmpty(miPushMessage.getUserAccount())) {
            mUserAccount = miPushMessage.getUserAccount();
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        mMessage = miPushMessage.getContent();
        GreenDaoApplication.getApplication().Extra = mMessage;
        SharedPreferences sharedPreferences =context.getSharedPreferences("Extra", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("extra", mMessage);
        editor.apply();
        Log.e("MessageArrived", mMessage);
        StorageUtils.WriteObject("message.txt", new HistoryAccount("wujie", mMessage));
        if (!TextUtils.isEmpty(miPushMessage.getTopic())) {
            mTopic = miPushMessage.getTopic();
        } else if (!TextUtils.isEmpty(miPushMessage.getAlias())) {
            mAlias = miPushMessage.getAlias();
        } else if (!TextUtils.isEmpty(miPushMessage.getUserAccount())) {
            mUserAccount = miPushMessage.getUserAccount();
        }
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        mMessage = miPushMessage.getContent();
        GreenDaoApplication.getApplication().Extra = mMessage;
        Log.d("onReceiveMessage", mMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.d("RegisterResult", miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.d("onCommandResult", miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();

        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
}
