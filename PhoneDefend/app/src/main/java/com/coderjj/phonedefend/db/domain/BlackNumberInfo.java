package com.coderjj.phonedefend.db.domain;

/**
 * Created by Administrator on 2019/5/20.
 */

public class BlackNumberInfo {
    private String phone;
    private String mode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo [ phone="+phone+" ,mode="+mode+" ]";
    }
}
