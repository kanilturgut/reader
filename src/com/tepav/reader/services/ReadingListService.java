package com.tepav.reader.services;

import android.content.Context;
import android.util.Log;
import com.tepav.reader.models.Gunluk;
import com.tepav.reader.models.Haber;
import com.tepav.reader.models.User;
import com.tepav.reader.models.Yayin;
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
    private User currentUser;
    private BaseDao baseDao;

    public  static int PERSISTANCE_TYPE_FAVORITES = 10;
    public  static int PERSISTANCE_TYPE_READ_LIST = 11;
    private static int PERSISTANCE_TYPE_BOTH      = 12;

    private ReadingListService() {

    }

    private ReadingListService(Context context) {
        BaseDao.saveApplicationContext(context);
        baseDao = BaseDao.getInstance();

        LoginRegisterService loginRegisterService = LoginRegisterService.getInstance();
        currentUser = loginRegisterService.getCurrentUser();
    }

    public static ReadingListService getInstance(Context context) {
        if (instance == null)
            instance = new ReadingListService(context);

        return instance;
    }

    public void save(Haber haber , int type) {


        if (haber.getPersistanceType() == null) {
            haber.setPersistanceType(type);
            baseDao.getHaberDao().insert(haber);
        }
        else {
            if ( (haber.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                 (haber.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES) ) {
                haber.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getHaberDao().update(haber);
            }
        }

        currentUser.getHaberList().add(haber);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Gunluk gunluk , int type) {

        if (gunluk.getPersistanceType() == null) {
            gunluk.setPersistanceType(type);
            baseDao.getGunlukDao().insert(gunluk);
        }
        else {
            if ( (gunluk.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                 (gunluk.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES) ) {
                gunluk.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getGunlukDao().update(gunluk);
            }
        }

        currentUser.getGunlukList().add(gunluk);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Yayin yayin , int type) {

        if (yayin.getPersistanceType() == null) {
            yayin.setPersistanceType(type);
            baseDao.getYayinDao().insert(yayin);
        }
        else {
            if ( (yayin.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES && type == PERSISTANCE_TYPE_READ_LIST) ||
                 (yayin.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST && type == PERSISTANCE_TYPE_FAVORITES) ) {
                yayin.setPersistanceType(PERSISTANCE_TYPE_BOTH);
                baseDao.getYayinDao().update(yayin);
            }
        }

        currentUser.getYayinList().add(yayin);
        baseDao.getUserDao().update(currentUser);
    }

    public void update(Haber haber) {
        baseDao.getHaberDao().update(haber);
        baseDao.getUserDao().update(currentUser);
    }

    public void update(Gunluk gunluk) {
        baseDao.getGunlukDao().update(gunluk);
        baseDao.getUserDao().update(currentUser);
    }

    public void update(Yayin yayin) {
        baseDao.getYayinDao().update(yayin);
        baseDao.getUserDao().update(currentUser);
    }

    public void delete(Haber haber , int type) {

        if (haber.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                haber.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                haber.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getHaberDao().update(haber);
        }
        else {
            baseDao.getHaberDao().delete(haber);
            currentUser.getHaberList().remove(haber);
        }

        baseDao.getUserDao().update(currentUser);

    }

    public void delete(Gunluk gunluk , int type) {

        if (gunluk.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                gunluk.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                gunluk.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getGunlukDao().update(gunluk);
        }
        else {
            baseDao.getGunlukDao().delete(gunluk);
            currentUser.getGunlukList().remove(gunluk);
        }

        baseDao.getUserDao().update(currentUser);

    }

    public void delete(Yayin yayin , int type) {

        if (yayin.getPersistanceType() == PERSISTANCE_TYPE_BOTH) {
            if (type == PERSISTANCE_TYPE_FAVORITES)
                yayin.setPersistanceType(PERSISTANCE_TYPE_READ_LIST);
            else
                yayin.setPersistanceType(PERSISTANCE_TYPE_FAVORITES);

            baseDao.getYayinDao().update(yayin);
        }
        else {
            baseDao.getYayinDao().delete(yayin);
            currentUser.getYayinList().remove(yayin);
        }

        baseDao.getUserDao().update(currentUser);

    }

    public List getReadingList() {
        List readingList = new ArrayList();

        List<Haber>  haberList  = currentUser.getHaberList();
        List<Yayin>  yayinList  = currentUser.getYayinList();
        List<Gunluk> gunlukList = currentUser.getGunlukList();

        for (Haber haber : haberList) {
            if (haber.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST)
                readingList.add(haber);
        }

        for (Yayin yayin : yayinList) {
            if (yayin.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST)
                readingList.add(yayin);
        }

        for (Gunluk gunluk : gunlukList) {
            if (gunluk.getPersistanceType() == PERSISTANCE_TYPE_READ_LIST)
                readingList.add(gunluk);
        }

        return readingList;
    }

    public List getFavoritesList() {
        List readingList = new ArrayList();

        List<Haber>  haberList  = currentUser.getHaberList();
        List<Yayin>  yayinList  = currentUser.getYayinList();
        List<Gunluk> gunlukList = currentUser.getGunlukList();

        for (Haber haber : haberList) {
            if (haber.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES)
                readingList.add(haber);
        }

        for (Yayin yayin : yayinList) {
            if (yayin.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES)
                readingList.add(yayin);
        }

        for (Gunluk gunluk : gunlukList) {
            if (gunluk.getPersistanceType() == PERSISTANCE_TYPE_FAVORITES)
                readingList.add(gunluk);
        }

        return readingList;
    }

}
