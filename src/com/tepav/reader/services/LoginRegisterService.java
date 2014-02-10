package com.tepav.reader.services;

import android.content.Context;
import com.tepav.reader.models.User;
import com.tepav.reader.repositories.BaseDao;
import com.tepav.reader.repositories.UserDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 10/02/14
 * Time: 15:08
 */
public class LoginRegisterService {

    private static LoginRegisterService instance;

    private User currentUser;
    private UserDao userDao;

    private LoginRegisterService() {
        userDao = BaseDao.getInstance().getUserDao();
    }

    public static LoginRegisterService getInstance() {
        if (instance == null)
            instance = new LoginRegisterService();

        return instance;
    }


    public void login(String email , String password) {
        List<User> fetchedUsers  = userDao.queryBuilder().where(UserDao.Properties.Email.eq(email)).list();

        if (fetchedUsers == null || fetchedUsers.size() == 0 ) {
            User user = new User();
            user.setEmail(email);
            userDao.insert(user);
            this.currentUser = user;
        }
        else {
            this.currentUser = fetchedUsers.get(0);
        }

    }

    public User getCurrentUser() {
        return currentUser;
    }

}
