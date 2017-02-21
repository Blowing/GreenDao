package com.wujie.greendao.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wujie.greendao.R;
import com.wujie.greendao.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PictureActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.picture_rv)
    RecyclerView pictureRv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel_btn, R.id.ok_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                break;
            case R.id.ok_btn:
                break;
        }
    }
}
