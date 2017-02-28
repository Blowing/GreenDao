package com.wujie.greendao.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wujie.greendao.R;
import com.wujie.greendao.adapter.AlbumAllAdapter;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendao.view.RvGridDividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity {

    @BindView(R.id.album_rv)
    RecyclerView albumRv;

    private AlbumAllAdapter albumAllAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        albumRv.setLayoutManager(new GridLayoutManager(this,3));
        albumRv.addItemDecoration(new RvGridDividerDecoration(this));
//        albumRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
//        albumRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        albumRv.setAdapter(new AlbumAllAdapter(this, mApp.getAlbumList()));
    }
}
