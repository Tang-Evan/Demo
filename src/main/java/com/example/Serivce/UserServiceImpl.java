package com.example.Serivce;

import com.example.Dao.UserDao;
import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//用戶注冊、登入和密碼重置
public class UserServiceImpl implements UserSerivce {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByAccount(String uaccount){
        return userDao.findByUaccount(uaccount);
    }

    @Override
    public Boolean RegisterUser(User user) {
        User register = userDao.save(user);
        if (register != null) {
            System.out.println("新增用戶資料成功");
            return true;
        }else{
            System.out.println("新增用戶資料失敗");
            return false;
        }
    }

    @Override
    public Integer updateUserPassword(User user) {
        User update = userDao.save(user);
        if (update != null) {
            System.out.println("更新用戶資料成功");
            System.out.println(update);
            return 1;
        }else{
            System.out.println("更新用戶資料失敗");
            return -1;
        }
    }
}
