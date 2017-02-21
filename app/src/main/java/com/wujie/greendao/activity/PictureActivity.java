package com.wujie.greendao.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wujie.greendao.R;
import com.wujie.greendao.adapter.AlbumDirAdapter;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendao.model.AlbumModel;
import com.wujie.greendao.util.Utils;
import com.wujie.greendao.view.DividerItemListDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


public class PictureActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.picture_rv)
    RecyclerView pictureRv;

    private AlbumDirAdapter mAlbumDirAdapter ;
    private List<AlbumModel> albumModelList = new ArrayList<AlbumModel>();

    private HashMap<String, List<String>> mPictureMap;
    private HashMap<String, List<String>> mVideoMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        initRecyclerView();

    }

    private void initRecyclerView() {
        pictureRv.setLayoutManager(new LinearLayoutManager(this));
        pictureRv.addItemDecoration(new DividerItemListDecoration(this, DividerItemListDecoration.VERTICAL_LIST, 100));
//        mAlbumDirAdapter.setOnCheckListener(new AlbumDirAdapter.onCheckListener() {
//            @Override
//            public void onCheckChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
        mAlbumDirAdapter = new AlbumDirAdapter(mApp, albumModelList);
        pictureRv.setAdapter(mAlbumDirAdapter);
        Observable.combineLatest(Utils.getSystemPhotoList(this), Utils.getSystemVideoList(this),
                new Func2<HashMap<String, List<String>>, HashMap<String, List<String>>, HashMap<String, List<String>>>() {
                    @Override
                    public HashMap<String, List<String>> call(HashMap<String, List<String>> pictureMap, HashMap<String, List<String>> videoMap) {
//                        mPictureMap = pictureMap;
//                        mVideoMap = videoMap;
//                        mVideoMap.putAll(videoMap);
                        return pictureMap;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<HashMap<String, List<String>>>() {
                    @Override
                    public void call(HashMap<String, List<String>> stringListHashMap) {
                        Set<String> set = stringListHashMap.keySet();
                        albumModelList.clear();
                        for (String key:set
                             ) {
                            String mSrc = stringListHashMap.get(key).get(0);
                            String mAlbumName = new File(mSrc).getParentFile().getName();
                            int pictureSize = stringListHashMap.get(key).size();
                            AlbumModel model = new AlbumModel(mSrc, mAlbumName, pictureSize, 0);
                            albumModelList.add(model);
                        }
                        Log.e("size", albumModelList.get(0).getmSrc()+ albumModelList.size());
                        mAlbumDirAdapter.notifyChanged(albumModelList);
                       // pictureRv.setAdapter(new AlbumDirAdapter(mApp, albumModelList));
                    }
                });

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
