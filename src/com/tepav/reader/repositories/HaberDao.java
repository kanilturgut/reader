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

import com.tepav.reader.models.Haber;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HABER.
*/
public class HaberDao extends AbstractDao<Haber, Long> {

    public static final String TABLENAME = "HABER";

    /**
     * Properties of entity Haber.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Haber_id = new Property(1, String.class, "haber_id", false, "HABER_ID");
        public final static Property Htitle = new Property(2, String.class, "htitle", false, "HTITLE");
        public final static Property Hcontent = new Property(3, String.class, "hcontent", false, "HCONTENT");
        public final static Property Hdate = new Property(4, String.class, "hdate", false, "HDATE");
        public final static Property Dname = new Property(5, String.class, "dname", false, "DNAME");
        public final static Property Himage = new Property(6, String.class, "himage", false, "HIMAGE");
    };

    private DaoSession daoSession;

    private Query<Haber> user_HaberListQuery;

    public HaberDao(DaoConfig config) {
        super(config);
    }
    
    public HaberDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HABER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'HABER_ID' TEXT," + // 1: haber_id
                "'HTITLE' TEXT," + // 2: htitle
                "'HCONTENT' TEXT," + // 3: hcontent
                "'HDATE' TEXT," + // 4: hdate
                "'DNAME' TEXT," + // 5: dname
                "'HIMAGE' TEXT);"); // 6: himage
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HABER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Haber entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String haber_id = entity.getHaber_id();
        if (haber_id != null) {
            stmt.bindString(2, haber_id);
        }
 
        String htitle = entity.getHtitle();
        if (htitle != null) {
            stmt.bindString(3, htitle);
        }
 
        String hcontent = entity.getHcontent();
        if (hcontent != null) {
            stmt.bindString(4, hcontent);
        }
 
        String hdate = entity.getHdate();
        if (hdate != null) {
            stmt.bindString(5, hdate);
        }
 
        String dname = entity.getDname();
        if (dname != null) {
            stmt.bindString(6, dname);
        }
 
        String himage = entity.getHimage();
        if (himage != null) {
            stmt.bindString(7, himage);
        }
    }

    @Override
    protected void attachEntity(Haber entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Haber readEntity(Cursor cursor, int offset) {
        Haber entity = new Haber( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // haber_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // htitle
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // hcontent
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // hdate
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // dname
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // himage
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Haber entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHaber_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHtitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHcontent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHdate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHimage(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Haber entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Haber entity) {
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
    
    /** Internal query to resolve the "haberList" to-many relationship of User. */
    public List<Haber> _queryUser_HaberList(Long id) {
        synchronized (this) {
            if (user_HaberListQuery == null) {
                QueryBuilder<Haber> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                user_HaberListQuery = queryBuilder.build();
            }
        }
        Query<Haber> query = user_HaberListQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

}
