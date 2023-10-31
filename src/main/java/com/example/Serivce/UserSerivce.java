package com.example.Serivce;

import com.example.bean.User;

public interface UserSerivce {
    User getUserByAccount(String uaccount);
    Boolean RegisterUser(User user);
    Integer updateUserPassword(User user);
    // 其他方法
}
