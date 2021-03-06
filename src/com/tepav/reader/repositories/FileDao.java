package com.tepav.reader.repositories;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.tepav.reader.models.File;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FILE.
*/
public class FileDao extends AbstractDao<File, Long> {

    public static final String TABLENAME = "FILE";

    /**
     * Properties of entity File.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property File_id = new Property(1, String.class, "file_id", false, "FILE_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Url = new Property(3, String.class, "url", false, "URL");
        public final static Property Path = new Property(4, String.class, "path", false, "PATH");
        public final static Property Haber_id = new Property(5, String.class, "haber_id", false, "HABER_ID");
    };

    private Query<File> haber_FileListQuery;
    private Query<File> yayin_FileListQuery;

    public FileDao(DaoConfig config) {
        super(config);
    }

    public FileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FILE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'FILE_ID' TEXT," + // 1: file_id
                "'NAME' TEXT," + // 2: name
                "'URL' TEXT," + // 3: url
                "'PATH' TEXT," + // 4: path
                "'HABER_ID' TEXT);"); // 5: haber_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FILE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, File entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String file_id = entity.getFile_id();
        if (file_id != null) {
            stmt.bindString(2, file_id);
        }

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }

        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(5, path);
        }

        String haber_id = entity.getHaber_id();
        if (haber_id != null) {
            stmt.bindString(6, haber_id);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public File readEntity(Cursor cursor, int offset) {
        File entity = new File( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // file_id
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // url
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // path
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // haber_id
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, File entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFile_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHaber_id(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(File entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(File entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /** Internal query to resolve the "fileList" to-many relationship of Haber. */
    public List<File> _queryHaber_FileList(Long id) {
        synchronized (this) {
            if (haber_FileListQuery == null) {
                QueryBuilder<File> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                haber_FileListQuery = queryBuilder.build();
            }
        }
        Query<File> query = haber_FileListQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    /** Internal query to resolve the "fileList" to-many relationship of Yayin. */
    public List<File> _queryYayin_FileList(Long id) {
        synchronized (this) {
            if (yayin_FileListQuery == null) {
                QueryBuilder<File> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                yayin_FileListQuery = queryBuilder.build();
            }
        }
        Query<File> query = yayin_FileListQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

}

