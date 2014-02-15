package com.tepav.reader.services;

import android.content.Context;
import android.util.Log;
import com.tepav.reader.models.*;
import com.tepav.reader.repositories.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 10/02/14
 * Time: 14:18
 */
public class ReadingListService {

    private static ReadingListService instance;
    private BaseDao baseDao;

    public static int PERSISTANCE_TYPE_FAVORITES = 10;
    public static int PERSISTANCE_TYPE_READ_LIST = 11;
    private static int PERSISTANCE_TYPE_BOTH = 12;

    private ReadingListService() {

    }

    private ReadingListService(Context context) {
        BaseDao.saveApplicationContext(context);
        baseDao = BaseDao.getInstance();
    }

    public static ReadingListService getInstance(Context context) {
        if (instance == null)
            instance = new ReadingListService(context);

        return instance;
    }

    public void save(Haber haber, int type) {

        if (haber.getPersistanceType() == null) {
            haber.setPersistanceType(type);

            List<File> fileList = haber.getFileListWithoutQuery();

            for(File file : fileList) {
                file.setHaber_id(haber.getHaber_id());
                baseDao.getFileDao().insert(file);
            }

            baseDao.getHaberDao().insert(haber);
        } else {
            if ((haber.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                    (haber.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES)) {
                haber.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getHaberDao().update(haber);
            }
        }
    }

    public void save(Gunluk gunluk, int type) {

        if (gunluk.getPersistanceType() == null) {
            gunluk.setPersistanceType(type);
            baseDao.getGunlukDao().insert(gunluk);
        } else {
            if ((gunluk.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                    (gunluk.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES)) {
                gunluk.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getGunlukDao().update(gunluk);
            }
        }
    }

    public void save(Yayin yayin, int type) {

        if (yayin.getPersistanceType() == null) {
            yayin.setPersistanceType(type);
            baseDao.getYayinDao().insert(yayin);
        } else {
            if ((yayin.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                    (yayin.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES)) {
                yayin.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getYayinDao().update(yayin);
            }
        }
    }

    public List<File> getFileList(Haber haber) {

        List<File> fetchedFiles = baseDao.getFileDao().queryBuilder().where(FileDao.Properties.Haber_id.eq(haber.getHaber_id())).list();

        return fetchedFiles;
    }

    public void update(Haber haber) {

        Haber haberInList = baseDao.getHaberDao().queryBuilder().where(HaberDao.Properties.Haber_id.eq(haber.getHaber_id())).list().get(0);

        baseDao.getHaberDao().update(haberInList);
    }

    public void update(Gunluk gunluk) {

        Gunluk gunlukInList = baseDao.getGunlukDao().queryBuilder().where(GunlukDao.Properties.Gunluk_id.eq(gunluk.getGunluk_id())).list().get(0);

        baseDao.getGunlukDao().update(gunlukInList);
    }

    public void update(Yayin yayin) {

        Yayin yayinInList = baseDao.getYayinDao().queryBuilder().where(YayinDao.Properties.Yayin_id.eq(yayin.getYayin_id())).list().get(0);

        baseDao.getYayinDao().update(yayinInList);
    }

    public void delete(Haber haber, int type) {

        Haber haberInList = baseDao.getHaberDao().queryBuilder().where(HaberDao.Properties.Haber_id.eq(haber.getHaber_id())).list().get(0);

        if (haberInList.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                haberInList.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                haberInList.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getHaberDao().update(haberInList);
        } else {
            baseDao.getHaberDao().deleteByKey(haberInList.getId());
        }
    }

    public void delete(Gunluk gunluk, int type) {

        Gunluk gunlukInList = baseDao.getGunlukDao().queryBuilder().where(GunlukDao.Properties.Gunluk_id.eq(gunluk.getGunluk_id())).list().get(0);

        if (gunlukInList.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                gunlukInList.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                gunlukInList.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getGunlukDao().update(gunlukInList);
        } else {
            baseDao.getGunlukDao().deleteByKey(gunlukInList.getId());
        }
    }

    public void delete(Yayin yayin, int type) {

        Yayin yayinInList = baseDao.getYayinDao().queryBuilder().where(YayinDao.Properties.Yayin_id.eq(yayin.getYayin_id())).list().get(0);

        if (yayinInList.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                yayinInList.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                yayinInList.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getYayinDao().update(yayinInList);
        } else {
            baseDao.getYayinDao().deleteByKey(yayinInList.getId());
        }
    }

    public List getReadingList() {
        List readingList = new ArrayList();

        List<Haber> haberList = baseDao.getHaberDao().loadAll();
        List<Yayin> yayinList = baseDao.getYayinDao().loadAll();
        List<Gunluk> gunlukList = baseDao.getGunlukDao().loadAll() ;

        for (Haber haber : haberList) {
            if (haber.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST ||
                    haber.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(haber);
        }

        for (Yayin yayin : yayinList) {
            if (yayin.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST ||
                    yayin.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(yayin);
        }

        for (Gunluk gunluk : gunlukList) {
            if (gunluk.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST ||
                    gunluk.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(gunluk);
        }

        return readingList;
    }

    public List getFavoritesList() {
        List readingList = new ArrayList();

        List<Haber> haberList = baseDao.getHaberDao().loadAll();
        List<Yayin> yayinList = baseDao.getYayinDao().loadAll();
        List<Gunluk> gunlukList = baseDao.getGunlukDao().loadAll();

        for (Haber haber : haberList) {
            if (haber.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES ||
                    haber.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(haber);
        }

        for (Yayin yayin : yayinList) {
            if (yayin.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES ||
                    yayin.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(yayin);
        }

        for (Gunluk gunluk : gunlukList) {
            if (gunluk.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES ||
                    gunluk.getPersistanceType() == PERSISTANCE_TYPE_BOTH)
                readingList.add(gunluk);
        }

        return readingList;
    }

}
