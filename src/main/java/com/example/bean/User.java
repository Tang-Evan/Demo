package com.example.bean;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {


    @Column(name="uid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Id
    @Column(name="uaccount", length=255, unique = true,nullable = false)
    private String uaccount;

    @Column(name="upwd", length=255, nullable=false)
    private String upwd;

    @Column(name="umail", length=255, nullable=false)
    private String umail;


    public User() {}

    @Override
    public String toString() {
        return "User [uid=" + uid + ", uaccount=" + uaccount + ", upwd=" + upwd + ", umail=" + umail + "]";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUmail() {
        return umail;
    }

    public void setUmail(String umail) {
        if (umail != null && !umail.isEmpty()) {
            this.umail = umail;
        } else {
            throw new IllegalArgumentException("umail 不能为空值");
        }
    }


}
