package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.oliverr.bugtracker.entity.Notification;
import org.oliverr.bugtracker.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class NotificationRepository {

    private DB db;

    @Autowired
    public void setDb(DB db) { this.db = db; }

    public boolean isThereUnread(Long userId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(notification_id) FROM notifications WHERE user_id = "+userId+" AND isOpened = 0;");

        boolean res = false;
        try {
            while(rs.next()) {
                if(rs.getInt(1) <= 0) {
                    res = false;
                } else {
                    res = true;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList<Notification> getUnreadNotifications(long userId) {
        ArrayList<Notification> notifications = new ArrayList<>();

        ResultSet rs = db.executeQuery("SELECT * FROM notifications WHERE user_id = "+userId+" AND isOpened = 0 ORDER BY notification_id DESC;");
        try {
            while(rs.next()) {
                Notification p = new Notification();
                p.setNotificationId(rs.getLong(1));
                p.setUserId(rs.getLong(2));
                p.setMessage(rs.getString(3));
                p.setDatetime(rs.getString(4));
                p.setOpened(rs.getInt(5) == 1);

                notifications.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public ArrayList<Notification> getAllNotifications(long userId) {
        ArrayList<Notification> notifications = new ArrayList<>();

        ResultSet rs = db.executeQuery("SELECT * FROM notifications WHERE user_id = "+userId+" ORDER BY notification_id DESC;");
        try {
            while(rs.next()) {
                Notification p = new Notification();
                p.setNotificationId(rs.getLong(1));
                p.setUserId(rs.getLong(2));
                p.setMessage(rs.getString(3));
                p.setDatetime(rs.getString(4));
                p.setOpened(rs.getInt(5) == 1);

                notifications.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public Notification getNotificationById(Long notificationId) {
        Notification notification = null;
        ResultSet rs = db.executeQuery("SELECT * FROM notifications WHERE notification_id = "+notificationId+";");
        try {
            while(rs.next()) {
                notification = new Notification();
                notification.setNotificationId(rs.getLong(1));
                notification.setUserId(rs.getLong(2));
                notification.setMessage(rs.getString(3));
                notification.setDatetime(rs.getString(4));
                notification.setOpened(rs.getInt(5) == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notification;
    }

    public void markAsRead(Long notificationId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("UPDATE notifications SET isOpened = 1 WHERE notification_id = ?;");
            ps.setLong(1, notificationId);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeNotification(Long notificationId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("DELETE FROM notifications WHERE notification_id = ?;");
            ps.setLong(1, notificationId);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long userId, String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String current = formatter.format(date);
        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO notifications(user_id, message, sent, isOpened) VALUES (?, ?, ?, 0);");
            ps.setLong(1, userId);
            ps.setString(2, message);
            ps.setString(3, current);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
