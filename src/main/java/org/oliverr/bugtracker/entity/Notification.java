package org.oliverr.bugtracker.entity;

public class Notification {

    private Long notificationId;
    private Long userId;
    private String message;
    private String datetime;
    private boolean isOpened;

    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDatetime() { return datetime; }
    public void setDatetime(String datetime) { this.datetime = datetime; }

    public boolean isOpened() { return isOpened; }
    public void setOpened(boolean opened) { isOpened = opened; }

}
