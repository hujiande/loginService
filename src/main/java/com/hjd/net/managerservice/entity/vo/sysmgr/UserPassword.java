package com.hjd.net.managerservice.entity.vo.sysmgr;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    private String password;
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
