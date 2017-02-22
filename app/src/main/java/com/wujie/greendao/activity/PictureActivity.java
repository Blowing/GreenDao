package com.wujie.greendao.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wujie.greendao.R;
import com.wujie.greendao.adapter.AlbumDirAdapter;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendao.cons.Constants;
import com.wujie.greendao.model.AlbumModel;
import com.wujie.greendao.nohttp.NohttpRequestManager;
import com.wujie.greendao.util.Utils;
import com.wujie.greendao.view.DividerItemListDecoration;
import com.wujie.greendaogen.Backup;
import com.wujie.greendaogen.BackupDao;
import com.wujie.greendaogen.DaoMaster;
import com.wujie.greendaogen.DaoSession;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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


    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private BackupDao mBackupDao;


    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        initRecyclerView();
        initDB();
    }

    private void initRecyclerView() {
        pictureRv.setLayoutManager(new LinearLayoutManager(this));
        pictureRv.addItemDecoration(new DividerItemListDecoration(this, DividerItemListDecoration.VERTICAL_LIST, 100));
        mAlbumDirAdapter = new AlbumDirAdapter(mApp, albumModelList);
        pictureRv.setAdapter(mAlbumDirAdapter);
        Observable.combineLatest(Utils.getSystemPhotoList(this), Utils.getSystemVideoList(this),
                new Func2<HashMap<String, List<String>>, HashMap<String, List<String>>, HashMap<String, List<String>>>() {
                    @Override
                    public HashMap<String, List<String>> call(HashMap<String, List<String>> pictureMap, HashMap<String, List<String>> videoMap) {
                        mPictureMap = pictureMap;
                        mVideoMap = videoMap;
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

    private void initDB() {
        mHelper = new DaoMaster.DevOpenHelper(this, "backup-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        mBackupDao = mDaoSession.getBackupDao();
    }

    @OnClick({R.id.cancel_btn, R.id.ok_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                break;
            case R.id.ok_btn:
                showLoginDialog();
                break;
        }
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText loginEt = new EditText(this);
        builder.setView(loginEt, 20,0,20,0).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NohttpRequestManager.requestCookie(loginEt.getText().toString().trim(), callBack);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("请输入路由器的登录密码").show();
    }


    private void storeToDB(List<String> mlist) {
        for (String src: mlist
             ) {
            File file = new File(src);
            if(file.exists()) {
                String filename = file.getName();
                Backup backup = new Backup(null, filename, "/usb/sda1/Camera", src,  Double.valueOf(String.valueOf(file.length())),new Date(100),0, false);
                mBackupDao.insert(backup);
            }
        }

        upload();
    }

    private void upload() {
        QueryBuilder<Backup> queryBuilder = mBackupDao.queryBuilder().where(BackupDao.Properties.Up_status.eq(false));
        for (Backup src: queryBuilder.list()
                ) {
            NohttpRequestManager.requestUpload( src.getId().intValue(),new FileBinary(new File(src.getSrc())),upLoadCallBack);
        }
    }

    private OnResponseListener<String> upLoadCallBack =  new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            Log.d("nohttp", response.toString());
            QueryBuilder<Backup> queryBuilder = mBackupDao.queryBuilder().where(BackupDao.Properties.Id.eq(what));
            queryBuilder.list().get(0).setUp_status(true);
           mBackupDao.update(queryBuilder.list().get(0));

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    private OnResponseListener<String> callBack = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if(what == 0) {
                try {
                    JSONObject jsonObject = new JSONObject(response.get());
                    Constants.USB_STOKE = jsonObject.getString("stok");
                    Log.d("stok", Constants.USB_STOKE);
                    List<String> fileList = new ArrayList<>();
                    SparseBooleanArray sparseBooleanArray = mAlbumDirAdapter.getmCheckStates();
                    for (int i = 0; i < sparseBooleanArray.size(); i++) {
                        if(sparseBooleanArray.valueAt(i)) {
                            fileList.addAll(mPictureMap.get(new File(mAlbumDirAdapter.getmContentList().get(i).getmSrc()).getParentFile().getAbsolutePath()));
                        }
                    }

                    storeToDB(fileList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("nohttp", response.toString());
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }

        @Override
        public void onFinish(int what) {

        }
    };


}
