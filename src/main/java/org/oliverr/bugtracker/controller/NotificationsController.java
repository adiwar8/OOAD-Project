package org.oliverr.bugtracker.controller;

import org.oliverr.bugtracker.entity.Notification;
import org.oliverr.bugtracker.entity.User;
import org.oliverr.bugtracker.repository.NotificationRepository;
import org.oliverr.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Objects;

@Controller
public class NotificationsController {

    private final UserRepository ur;
    private final NotificationRepository nr;

    @Autowired
    public NotificationsController(UserRepository ur, NotificationRepository nr) {
        this.ur = ur;
        this.nr = nr;
    }

    @RequestMapping("/notifications")
    public String notifications(Model model, Principal principal) {
        User loggedUser = ur.findByEmail(principal.getName());
        model.addAttribute("user", loggedUser);
        model.addAttribute("isAdmin", ur.isAdmin(loggedUser));
        model.addAttribute("pageTitle", "Notifications | Bug Tracker");
        model.addAttribute("isUnread", nr.isThereUnread(loggedUser.getId()));

        model.addAttribute("notifications", nr.getAllNotifications(loggedUser.getId()));

        return "notifications";
    }

    @RequestMapping("/notification/{notificationId}")
    public String notification(Model model, Principal principal, @PathVariable(value="notificationId") String notificationId) {
        User user = ur.findByEmail(principal.getName());
        try {
            Notification n = nr.getNotificationById(Long.parseLong(notificationId));
            if(Objects.equals(n.getUserId(), user.getId())) {
                if(n != null) {
                    model.addAttribute("notification", n);

                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", ur.isAdmin(user));
                    model.addAttribute("pageTitle", "Notification #"+notificationId+" | Bug Tracker");
                    model.addAttribute("isUnread", nr.isThereUnread(user.getId()));

                    if(!n.isOpened()) nr.markAsRead(Long.parseLong(notificationId));
                    return "notification";
                }
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
        return "error";
    }

    @RequestMapping(value = "/notification/remove", method = RequestMethod.POST)
    public String removeNotification(@ModelAttribute Notification notification) {
        nr.removeNotification(notification.getNotificationId());
        return "redirect:/notifications";
    }

}
