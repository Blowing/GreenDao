package com.wujie.greendao.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import com.wujie.greendao.R;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendao.util.Utils;
import com.wujie.greendaogen.BackupDao;
import com.wujie.greendaogen.DaoMaster;
import com.wujie.greendaogen.DaoSession;
import com.wujie.greendaogen.MigrationHelper;
import com.wujie.greendaogen.MySQLiteOpenHelper;
import com.wujie.greendaogen.Person;
import com.wujie.greendaogen.PersonDao;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.et_token)
    EditText etToken;

    @BindView(R.id.simple_view)
    SimpleDraweeView simpleDraweeView;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private PersonDao mPersonDao;
    private Cursor cursor;

    private BackupDao mBackupDao;
    private SimpleCursorAdapter mCursorAdapter;

    private String mName;
    private String mSex;
    private String mId;
    private String mHeight = "1.81";

    private Subscription  subscription;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission("");
            requestPermissions(new String[2], 3);
            shouldShowRequestPermissionRationale("String");
            onRequestPermissionsResult(1, new String[2], new int[2]);

        }
        ButterKnife.bind(this);
        context = this;
        MigrationHelper.DEBUG = true;
        MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(this, "test.db",
                null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
//        mHelper = new DaoMaster.DevOpenHelper(this, "backup-db", null);
        db = mHelper.getWritableDatabase();
//        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        mPersonDao = mDaoSession.getPersonDao();
        mBackupDao = mDaoSession.getBackupDao();
        cursor = db.query(mPersonDao.getTablename(), mPersonDao.getAllColumns(), null, null, null, null, null);
        String[] from = {PersonDao.Properties.Id.columnName, PersonDao.Properties.Name.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, Adapter.NO_SELECTION);
        listView.setAdapter(mCursorAdapter);
        getSupportFragmentManager().beginTransaction().commitNowAllowingStateLoss();
        Utils.isMIUI();
        Utils.isEMUI();
        Utils.isFlyme();

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("customscheme://com.huawei.pushtext/notify_detail?"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.i("intent", intentUri);
        Intent intent1 = new Intent(context, NotificationActivity.class);
        String uriString = intent1.toUri(Intent.URI_INTENT_SCHEME);
        Log.i("intent1", uriString);
        Log.i("xiaomi", "RegisterId+" + MiPushClient.getRegId(this));
        simpleDraweeView.setImageURI(Uri.parse("http://192.168.10.84/test/picture/v1.png"));

    }

    @OnClick({R.id.btn_add, R.id.btn_delete, R.id.btn_search, R.id.btn_update,R.id.btn_xiaomi, R.id.btn_huawei,
            R.id.btn_backup})
    public void onClick(View view) {
        mName = etName.getText().toString().trim();
        mSex = etSex.getText().toString().trim();
        mId = etId.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_backup:
                if(AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)) {
                    startActivity(new Intent(this, PictureActivity.class));
                } else {
                    AndPermission.with(this)
                            .requestCode(100)
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE)
                            .send();
                }
                break;
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_search:
                search();
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_xiaomi:
                etToken.setText(MiPushClient.getRegId(this));
                break;
            case R.id.btn_huawei:
                subscription = Observable.interval(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                if(!mApp.HuaweiToken.equals("")) {
                                    etToken.setText(mApp.HuaweiToken);
                                    subscription.unsubscribe();
                                }
                            }
                        });
                break;
        }
        cursor = db.query(mPersonDao.getTablename(), mPersonDao.getAllColumns(), null, null, null, null, null);
        mCursorAdapter.swapCursor(cursor);
    }

    private void update() {
        mPersonDao.update(new Person(Long.valueOf(mId), mName, mSex, mHeight));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);

    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == 100) {
                // TODO 相应代码。
                startActivity(new Intent(MainActivity.this, PictureActivity.class));
            } else if(requestCode == 101) {
                // TODO 相应代码。
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                //AndPermission.defaultSettingDialog(this, AndPermission.REQUEST_CODE_SETTING).show();

                 //第二种：用自定义的提示语。
                 AndPermission.defaultSettingDialog(MainActivity.this, 12)
                 .setTitle("权限申请失败")
                 .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                 .setPositiveButton("好，去设置")
                 .show();

                // 第三种：自定义dialog样式。
                // SettingService settingService =
                //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
                // 你的dialog点击了确定调用：
                // settingService.execute();
                // 你的dialog点击了取消调用：
                // settingService.cancel();
            }
        }
    };

    private void search() {
        QueryBuilder<Person> queryBuilder = mPersonDao.queryBuilder().where(PersonDao.Properties.Name.eq(mName));
        List<Person> persons = queryBuilder.list();
        new AlertDialog.Builder(this).setMessage(persons != null && persons.size() > 0 ?
                persons.get(0).getName() + "-->" + persons.get(0).getSex() : "查无此数据").setPositiveButton("确定", null).create().show();

    }

    private void delete() {
        mPersonDao.deleteByKey(Long.valueOf(mId));
    }

    private void add() {
        Person person = new Person(null, mName, mSex, mHeight);
        mPersonDao.insert(person);
    }
}
