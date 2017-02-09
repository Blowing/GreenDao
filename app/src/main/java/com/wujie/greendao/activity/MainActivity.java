package com.wujie.greendao.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.wujie.greendao.R;
import com.wujie.greendao.base.BaseActivity;
import com.wujie.greendaogen.DaoMaster;
import com.wujie.greendaogen.DaoSession;
import com.wujie.greendaogen.Person;
import com.wujie.greendaogen.PersonDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.list_view)
    ListView listView;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private PersonDao mPersonDao;
    private Cursor cursor;
    private SimpleCursorAdapter mCursorAdapter;

    private String mName;
    private String mSex;
    private String mId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mHelper = new DaoMaster.DevOpenHelper(this, "test-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        mPersonDao = mDaoSession.getPersonDao();
        cursor = db.query(mPersonDao.getTablename(), mPersonDao.getAllColumns(), null, null, null, null, null);
        String[] from = {PersonDao.Properties.Name.columnName, PersonDao.Properties.Sex.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to , Adapter.NO_SELECTION);
        listView.setAdapter(mCursorAdapter);
    }

    @OnClick({R.id.btn_add, R.id.btn_delete, R.id.btn_search, R.id.btn_update})
    public void onClick(View view) {
        mName = etName.getText().toString().trim();
        mSex = etSex.getText().toString().trim();
        mId = etId.getText().toString().trim();
        switch (view.getId()) {
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
        }
        cursor = db.query(mPersonDao.getTablename(), mPersonDao.getAllColumns(), null, null, null, null, null);
        mCursorAdapter.swapCursor(cursor);
    }

    private void update() {
        mPersonDao.update(new Person(Long.valueOf(mId), mName, mSex));

    }

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
        Person person = new Person(null, mName, mSex);
        mPersonDao.insert(person);
    }
}
