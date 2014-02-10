package com.tepav.reader.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 10/02/14
 * Time: 14:57
 */
public class BaseDao {

    private final String DB = "tepav";

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private static BaseDao instance;
    private static Context applicationContext;

    private UserDao userDao;
    private HaberDao haberDao;
    private GunlukDao gunlukDao;
    private YayinDao yayinDao;

    private BaseDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(applicationContext , DB , null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        userDao      = daoSession.getUserDao();
        haberDao     = daoSession.getHaberDao();
        gunlukDao    = daoSession.getGunlukDao();
        yayinDao     = daoSession.getYayinDao();

        applicationContext = null;
    }

    public static BaseDao getInstance() {

        if (instance == null) {
            instance = new BaseDao();
        }

        return instance;
    }

    public static void saveApplicationContext(Context context) {
        applicationContext = context;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public HaberDao getHaberDao() {
        return haberDao;
    }

    public GunlukDao getGunlukDao() {
        return gunlukDao;
    }

    public YayinDao getYayinDao() {
        return yayinDao;
    }
}
