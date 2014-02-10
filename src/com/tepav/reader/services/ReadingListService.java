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

    public void save(Haber haber) {
        baseDao.getHaberDao().insert(haber);
        currentUser.getHaberList().add(haber);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Gunluk gunluk) {
        baseDao.getGunlukDao().insert(gunluk);
        currentUser.getGunlukList().add(gunluk);
        baseDao.getUserDao().update(currentUser);
    }

    public void save(Yayin yayin) {
        baseDao.getYayinDao().insert(yayin);
        currentUser.getYayinList().add(yayin);
        baseDao.getUserDao().update(currentUser);
    }

    public void delete(Haber haber) {
        baseDao.getHaberDao().delete(haber);
    }

    public void delete(Gunluk gunluk) {
        baseDao.getGunlukDao().delete(gunluk);
    }

    public void delete(Yayin yayin) {
        baseDao.getYayinDao().delete(yayin);
    }

    public List getReadingList() {
        List readingList = new ArrayList();
        readingList.addAll(currentUser.getHaberList());
        readingList.addAll(currentUser.getYayinList());
        readingList.addAll(currentUser.getGunlukList());

        return readingList;
    }

}
