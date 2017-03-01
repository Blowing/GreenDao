package com.wujie.greendaogen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wujie on 2017/3/1.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper{

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, PersonDao.class);
    }
}
