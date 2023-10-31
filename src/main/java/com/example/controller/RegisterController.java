package com.example.controller;

import com.example.Serivce.UserSerivce;
import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
    @Autowired
    private UserSerivce userSerivce;

    //用於處理GET請求，顯示登入頁面
    @GetMapping("/register")
    public String showRegisterFrom(){
        return "register"; //返回登入頁面的試圖名稱(register.jsp)
    }

    //用於處理POST請求，驗證用戶名和密碼
    @PostMapping("/register")
    public ResponseEntity<?> processRegister(@RequestBody User user){
        //已存在用戶判斷
        User existingUser = userSerivce.getUserByAccount(user.getUaccount());
        if(existingUser != null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Username already exists!\"}");
        }

        // 创建一个 User 对象并设置属性
        String md5upwd = DigestUtils.md5DigestAsHex(user.getUpwd().getBytes());
        user.setUaccount(user.getUaccount());
        user.setUpwd(md5upwd);
        user.setUmail(user.getUmail());

        // 调用 userDao.RegisterUser(user) 方法
        boolean registrationResult = userSerivce.RegisterUser(user);

        // 根据注册结果返回不同的视图
        if (registrationResult) {
            // 注册成功，返回成功页面
            return ResponseEntity.ok("{\"message\": \"Success! Back to Login Page!\"}");
        } else {
            // 注册失败，返回失败页面
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Registration failed.\"}");
        }
    }
}
