package com.example.controller;

import com.example.Serivce.UserSerivce;
import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

@RestController

public class LoginController {
    @Autowired
    private  UserSerivce userSerivce;

    //Portal
    @GetMapping("/")
    public String portal() {
        return "login";
    }

    //用於處理GET請求，顯示登入頁面
    @GetMapping("/login")
    public String showLoginFrom(){
        return "login"; //返回登入頁面的試圖名稱(login.jsp)
    }

    @GetMapping("/checkAccount")
    public ResponseEntity<?> checkAccount(@RequestParam String uaccount) {
        System.out.println("賬號： "+ uaccount);
        User user = userSerivce.getUserByAccount(uaccount);
        if (user != null) {
            return ResponseEntity.ok("{\"message\": \"Acccount Exist!\"}");
        }else if(uaccount ==""){
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.ok("{\"message\": \"Account can Use!\"}");
        }
    }

    /**用於處理POST請求，驗證用戶名和密碼*/
    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@RequestBody User user){
        // 这里可以添加验证逻辑，检查用户名和密码是否正确
        // 如果验证成功，可以重定向到用户的主页或其他页面
        // 如果验证失败，可以返回登录页面并显示错误消息
        System.out.println("OPEN!");
        String md5upwd = DigestUtils.md5DigestAsHex(user.getUpwd().getBytes());
        User user_exist = userSerivce.getUserByAccount(user.getUaccount());
        if(user_exist != null && user_exist.getUpwd().equals(md5upwd)){
            // 登录成功，返回成功响应，HttpStatus.OK 表示成功
            return ResponseEntity.ok(user_exist);
        }else {
            // 登录失败，返回错误响应，HttpStatus.UNAUTHORIZED 表示未经授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password.\"}");
        }




        // 示例：假设用户名和密码都是 "admin"
//        if ("admin".equals(uaccount) && "password".equals(upwd)) {
//            // 登录成功，重定向到主页
//            return "home";
//        } else {
//            // 登录失败，返回登录页面并显示错误消息
//            model.addAttribute("error", "Invalid username or password.");
//            return "login";
//        }
    }
    //處理登入請求的方法
}
