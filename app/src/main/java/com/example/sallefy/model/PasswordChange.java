package com.example.sallefy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PasswordChange implements Serializable {

    @SerializedName("currentPassword")
    @Expose
    private String currentPassword;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    private final static long serialVersionUID = 6291463140628534394L;

    /**
     * No args constructor for use in serialization
     */
    public PasswordChange() {
    }

    /**
     * @param newPassword
     * @param currentPassword
     */
    public PasswordChange(String currentPassword, String newPassword) {
        super();
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}