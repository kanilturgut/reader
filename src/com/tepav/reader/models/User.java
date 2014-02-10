package com.tepav.reader.models;

import java.util.List;

import com.tepav.reader.repositories.*;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER.
 */
public class User {

    private Long id;
    private String email;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserDao myDao;

    private List<Haber>  haberList;
    private List<Gunluk> gunlukList;
    private List<Yayin>  yayinList;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Haber> getHaberList() {
        if (haberList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HaberDao targetDao = daoSession.getHaberDao();
            List<Haber> haberListNew = targetDao._queryUser_HaberList(id);
            synchronized (this) {
                if(haberList == null) {
                    haberList = haberListNew;
                }
            }
        }
        return haberList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetHaberList() {
        haberList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Gunluk> getGunlukList() {
        if (gunlukList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GunlukDao targetDao = daoSession.getGunlukDao();
            List<Gunluk> gunlukListNew = targetDao._queryUser_GunlukList(id);
            synchronized (this) {
                if(gunlukList == null) {
                    gunlukList = gunlukListNew;
                }
            }
        }
        return gunlukList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGunlukList() {
        gunlukList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Yayin> getYayinList() {
        if (yayinList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            YayinDao targetDao = daoSession.getYayinDao();
            List<Yayin> yayinListNew = targetDao._queryUser_YayinList(id);
            synchronized (this) {
                if(yayinList == null) {
                    yayinList = yayinListNew;
                }
            }
        }
        return yayinList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetYayinList() {
        yayinList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}