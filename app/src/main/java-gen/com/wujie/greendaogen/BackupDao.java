package com.wujie.greendaogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BACKUP".
*/
public class BackupDao extends AbstractDao<Backup, Long> {

    public static final String TABLENAME = "BACKUP";

    /**
     * Properties of entity Backup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Filename = new Property(1, String.class, "filename", false, "FILENAME");
        public final static Property D_src = new Property(2, String.class, "d_src", false, "D_SRC");
        public final static Property Src = new Property(3, String.class, "src", false, "SRC");
        public final static Property Size = new Property(4, double.class, "size", false, "SIZE");
        public final static Property Date = new Property(5, java.util.Date.class, "date", false, "DATE");
        public final static Property Progress = new Property(6, Integer.class, "progress", false, "PROGRESS");
        public final static Property Up_status = new Property(7, Boolean.class, "up_status", false, "UP_STATUS");
    }


    public BackupDao(DaoConfig config) {
        super(config);
    }
    
    public BackupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BACKUP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FILENAME\" TEXT NOT NULL ," + // 1: filename
                "\"D_SRC\" TEXT NOT NULL ," + // 2: d_src
                "\"SRC\" TEXT NOT NULL ," + // 3: src
                "\"SIZE\" REAL NOT NULL ," + // 4: size
                "\"DATE\" INTEGER," + // 5: date
                "\"PROGRESS\" INTEGER," + // 6: progress
                "\"UP_STATUS\" INTEGER);"); // 7: up_status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BACKUP\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Backup entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFilename());
        stmt.bindString(3, entity.getD_src());
        stmt.bindString(4, entity.getSrc());
        stmt.bindDouble(5, entity.getSize());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(6, date.getTime());
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(7, progress);
        }
 
        Boolean up_status = entity.getUp_status();
        if (up_status != null) {
            stmt.bindLong(8, up_status ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Backup entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFilename());
        stmt.bindString(3, entity.getD_src());
        stmt.bindString(4, entity.getSrc());
        stmt.bindDouble(5, entity.getSize());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(6, date.getTime());
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(7, progress);
        }
 
        Boolean up_status = entity.getUp_status();
        if (up_status != null) {
            stmt.bindLong(8, up_status ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Backup readEntity(Cursor cursor, int offset) {
        Backup entity = new Backup( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // filename
            cursor.getString(offset + 2), // d_src
            cursor.getString(offset + 3), // src
            cursor.getDouble(offset + 4), // size
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // date
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // progress
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0 // up_status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Backup entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFilename(cursor.getString(offset + 1));
        entity.setD_src(cursor.getString(offset + 2));
        entity.setSrc(cursor.getString(offset + 3));
        entity.setSize(cursor.getDouble(offset + 4));
        entity.setDate(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setProgress(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setUp_status(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Backup entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Backup entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Backup entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}