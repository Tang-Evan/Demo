package com.example.controller;

import com.example.Serivce.UserSerivce;
import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Properties;

@RestController
public class ForgotPasswordController {

    @Autowired
    private UserSerivce userSerivce;

    //用於處理GET請求，顯示登入頁面
    @GetMapping("/forgotPassword")
    public String showRegisterFrom(){
        return "forgotPassword"; //返回登入頁面的試圖名稱(forgotPassword.jsp)
    }

    //用於處理POST請求，驗證用戶名和密碼
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> processUpdate(@RequestBody User user){
        //不存在用戶信息
        User NoexistUser = userSerivce.getUserByAccount(user.getUaccount());
        if(NoexistUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Username doesn't exist!\"}");
        }

        // 创建一个 User 对象并设置属性
        String newpwd = generateRandomPassword(6); //明文
        String md5newupwd = DigestUtils.md5DigestAsHex(newpwd.getBytes()); //MD5加密
        System.out.println("新密碼是： " + newpwd);
        User user_update = userSerivce.getUserByAccount(user.getUaccount());
        String umail= user_update.getUmail();
        user_update.setUaccount(user.getUaccount());
        user_update.setUpwd(md5newupwd);
        user_update.setUmail(umail);
        //先发送新密码到邮箱再update密码
        boolean emailSent = EmailSender(umail, newpwd);
        System.out.println("寄郵件成功！");
        if(emailSent){
            // 调用 userDao.updateUserPassword(user) 方法
            int updateResult = userSerivce.updateUserPassword(user_update);
            System.out.println(updateResult);
            // 根据注册结果返回不同的视图
            if (updateResult == 1) {
                // 更新成功，返回成功页面
                //*添加email寄送新密碼(新密碼是 newpwd,明文密碼！)的功能再return登入頁面
                System.out.println("更新成功");
                System.out.println("獲取到的email是： " + umail);
                return ResponseEntity.ok("{\"message\": \"Success! Back to Login Page!\"}");
            }
        }
        // 邮件发送失败或更新失败，返回相应的错误信息
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Email sending or update failed\"}");
    }

    // 生成特定長度的隨機字母數字密碼的方法
    private static String generateRandomPassword(int len)
    {
        // ASCII 範圍 – 字母數字 (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // 循環的每次迭代從給定的字符中隨機選擇一個字符
        // ASCII 範圍並將其附加到 `StringBuilder` 實例

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }



    private static boolean EmailSender(String umail, String newpassword){
        final String mailname = "aven814@gmail.com";
        final String mailpassword = "ncizqzwjwwltzcde";

        //配置郵件服務器信息
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", "smtp.gmail.com"); //使用Gmail SMTP服務器
        props.put("mail.smtp.port", "587"); //Gmail SMTP 端口

        //創建Session對象
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailname, mailpassword);
                    }
                });
        try{
            //創建消息對象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailname));//發件人地址
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(umail)); //收件人地址
            message.setSubject("密碼重置"); //郵件主題
            message.setText("您的新密碼是： " + newpassword); //郵件正文

            //發送郵件
            Transport.send(message);

            System.out.println("電子郵件已發送成功！");
            return true;

        } catch (AddressException e) {
            e.printStackTrace();
            System.out.println("無此帳號存在");
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

}
