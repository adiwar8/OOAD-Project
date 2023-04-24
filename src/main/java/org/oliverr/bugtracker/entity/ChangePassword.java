package org.oliverr.bugtracker.entity;

public class ChangePassword {

    String oldPassword;
    String newPassword;
    String newPasswordAgain;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getNewPasswordAgain() { return newPasswordAgain; }
    public void setNewPasswordAgain(String newPasswordAgain) { this.newPasswordAgain = newPasswordAgain; }

}
