package com.wujie.greendao.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.wujie.greendao.cons.GreenDaoApplication;

/**
 * Created by wujie on 2017/2/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements HuaweiApiClient.ConnectionCallbacks,
        HuaweiApiClient.OnConnectionFailedListener, HuaweiApiAvailability.OnUpdateListener {

    public final static String TAG = "huaweipush";

    public static final int RECEIVE_PUSH_MSG = 0x100;

    public static final int RECEIVE_TOKEN_MSG = 0x101;

    public static final int RECEIVE_NOTIFY_CLICK_MSG = 0x102;

    public static final int RECEIVE_TAG_MSG = 0x103;

    public static final int RECEIVE_STATUS_MSG = 0x104;

    public static final int OTHER_MSG = 0x105;

    public static final String NORMAL_MSG_ENABLE = "normal_msg_enable";

    public static final String NOTIFY_MSG_ENABLE = "notify_msg_enable";

    public static HuaweiApiClient huaweiApiClient;

    protected GreenDaoApplication mApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        huaweiApiClient = new HuaweiApiClient.Builder(this).addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        huaweiApiClient.connect();
        mApp = (GreenDaoApplication) getApplication();
        getToken();

        new Thread() {
            @Override
            public void run() {
                setNotifyMsgEnable(true);
                setPassByMsg(true);
            }
        }.start();
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("");
//            }
//        }).observeOn(Schedulers.newThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        setNotifyMsgEnable(true);
//                        setPassByMsg(true);
//                    }
//                });


    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart, ErrorCode:" + HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(this));
        super.onStart();

        huaweiApiClient.connect();
        getToken();
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "onConnected, IsConnected: " + huaweiApiClient.isConnected());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed, ErrorCode:" + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended, cause" + i + ", IsConnected:" + huaweiApiClient.isConnected());
    }

    @Override
    public void onUpdateFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onUpdateFailed, ErrorCode: " + connectionResult.getErrorCode());
    }

    public void setPassByMsg(boolean flag) {
        HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(huaweiApiClient, flag);
    }

    public void setNotifyMsgEnable(boolean flag) {
        HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(huaweiApiClient, flag);
    }

    private void getToken() {
        if (!isConnected()) {
            Log.d(TAG, "get token failed, HMS is disconnect.");
            return;
        }

        // 异步调用方式
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(huaweiApiClient);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

            @Override
            public void onResult(TokenResult result) {
                mApp.HuaweiToken = result.getTokenRes().getToken();
                //Log.i(BaseActivity.TAG, "wujie" + result.getTokenRes().getToken());
            }

        });
    }

    public static boolean isConnected() {
        if (huaweiApiClient != null && huaweiApiClient.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}

