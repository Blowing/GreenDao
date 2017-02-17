package com.wujie.greendao.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wujie.greendao.R;
import com.wujie.greendao.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.tv_extra)
    TextView tvExtra;
    Subscription s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        s = Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "extra"+ aLong+""+mApp.Extra);
                        if(!mApp.Extra.equals(""))  {
                            tvExtra.setText(mApp.Extra);
                            s.unsubscribe();
                        } else if (aLong.intValue()> 40) {
                            s.unsubscribe();
                        }
//                        } else {
//                            SharedPreferences sp = getSharedPreferences("Extra", Context.MODE_PRIVATE);
//                            tvExtra.setText(sp.getString("extra", "wujie"));
//                        }
                    }
                });


    }
}
