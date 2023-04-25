package org.oliverr.bugtracker.controller;

import org.oliverr.bugtracker.entity.Todo;
import org.oliverr.bugtracker.entity.User;
import org.oliverr.bugtracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    private final UserRepository ur;
    private final NotificationRepository nr;
    private final TodoRepository todor;
    private final ProjectRepository pr;
    private final TaskRepository tr;
    private final BugRepository br;

    @Autowired
    public DashboardController(UserRepository ur, NotificationRepository nr, TodoRepository todor, ProjectRepository pr, TaskRepository tr, BugRepository br) {
        this.ur = ur;
        this.nr = nr;
        this.todor = todor;
        this.pr = pr;
        this.tr = tr;
        this.br = br;
    }

    @RequestMapping("/")
    public String dashboard(Model model, Principal principal) {
        User loggedUser = ur.findByEmail(principal.getName());
        model.addAttribute("user", loggedUser);
        model.addAttribute("isAdmin", ur.isAdmin(loggedUser));
        model.addAttribute("pageTitle", "Dashboard | Bug Tracker");
        model.addAttribute("isUnread", nr.isThereUnread(loggedUser.getId()));

        model.addAttribute("projectsCount", pr.projectsCount(loggedUser.getId()));
        model.addAttribute("tasksCount", tr.tasksCount(loggedUser.getId()));
        model.addAttribute("bugsCount", br.bugsCount(loggedUser.getId()));
        model.addAttribute("todos", todor.getTodos(loggedUser.getId()));

        model.addAttribute("uncompletedBugs", br.uncompletedBugsCount(loggedUser.getId()));
        model.addAttribute("uncompletedTasks", tr.uncompletedTaskCount(loggedUser.getId()));

        model.addAttribute("projects", pr.getProjects(loggedUser.getId()));
        model.addAttribute("unreadNotification", nr.getUnreadNotifications(loggedUser.getId()));

        model.addAttribute("todo", new Todo());
        ur.updateLastLoginDate(loggedUser.getId());

        return "index";
    }

}
