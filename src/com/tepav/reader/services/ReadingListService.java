package com.tepav.reader.services;

import android.content.Context;
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

    public static int PERSISTANCE_TYPE_FAVORITES = 0;
    public static int PERSISTANCE_TYPE_READ_LIST = 1;

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
        haber.setPersistanceType(type);
        baseDao.getHaberDao().insert(haber);
        currentUser.getHaberList().add(haber);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Gunluk gunluk , int type) {
        gunluk.setPersistanceType(type);
        baseDao.getGunlukDao().insert(gunluk);
        currentUser.getGunlukList().add(gunluk);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Yayin yayin , int type) {
        yayin.setPersistanceType(type);
        baseDao.getYayinDao().insert(yayin);
        currentUser.getYayinList().add(yayin);
        baseDao.getUserDao().update(currentUser);
    }

    public void delete(Haber haber) {
        baseDao.getHaberDao().delete(haber);
        currentUser.getHaberList().remove(haber);
        baseDao.getUserDao().update(currentUser);
    }

    public void delete(Gunluk gunluk) {
        baseDao.getGunlukDao().delete(gunluk);
        currentUser.getGunlukList().remove(gunluk);
        baseDao.getUserDao().update(currentUser);
    }

    public void delete(Yayin yayin) {
        baseDao.getYayinDao().delete(yayin);
        currentUser.getYayinList().remove(yayin);
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
