package org.oliverr.bugtracker.controller;

import org.oliverr.bugtracker.entity.Bug;
import org.oliverr.bugtracker.entity.User;
import org.oliverr.bugtracker.repository.BugRepository;
import org.oliverr.bugtracker.repository.NotificationRepository;
import org.oliverr.bugtracker.repository.ProjectRepository;
import org.oliverr.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class BugsController {

    private final UserRepository ur;
    private final BugRepository br;
    private final ProjectRepository pr;
    private final NotificationRepository nr;

    @Autowired
    public BugsController(UserRepository ur, BugRepository br, ProjectRepository pr, NotificationRepository nr) {
        this.ur = ur;
        this.br = br;
        this.pr = pr;
        this.nr = nr;
    }

    @RequestMapping("/bugs")
    public String bugs(Model model, Principal principal) {
        User loggedUser = ur.findByEmail(principal.getName());
        model.addAttribute("user", loggedUser);
        model.addAttribute("isAdmin", ur.isAdmin(loggedUser));
        model.addAttribute("pageTitle", "Bugs | Bug Tracker");
        model.addAttribute("isUnread", nr.isThereUnread(loggedUser.getId()));

        model.addAttribute("allBug", br.getAllBugs(loggedUser.getId()));
        model.addAttribute("projects", pr.getAllProjects(loggedUser.getId()));

        model.addAttribute("bug", new Bug());

        return "bugs";
    }

    @RequestMapping("/bugs/project/{projectid}")
    public String projectBug(Model model, Principal principal, @PathVariable(value="projectid") String projectid) {
        User loggedUser = ur.findByEmail(principal.getName());
        try {
            if(pr.isItTheirProject(Long.parseLong(projectid), loggedUser.getId())) {
                model.addAttribute("user", loggedUser);
                model.addAttribute("isAdmin", ur.isAdmin(loggedUser));
                model.addAttribute("pageTitle", "Bugs | Bug Tracker");
                model.addAttribute("isUnread", nr.isThereUnread(loggedUser.getId()));

                model.addAttribute("allBug", br.getProjectBugs(Long.parseLong(projectid)));

                model.addAttribute("projects", pr.getAllProjects(loggedUser.getId()));
                model.addAttribute("bug", new Bug());

                return "bugs";
            } else {
                return "error";
            }
        } catch(Exception e) {
            return "error";
        }
    }

    @RequestMapping(value = "/bugs/add", method = RequestMethod.POST)
    public String addBug(@ModelAttribute Bug bug, Principal principal) {
        User sender = ur.findByEmail(principal.getName());

        br.addBug(pr.getProjectIdByName(bug.getProjectName()), sender.getId(), bug.getTitle(), bug.getDescription(), bug.getStatus());

        return "redirect:/bugs";
    }

    @RequestMapping("/bug/{bugid}")
    public String bug(Model model, @PathVariable(value="bugid") String bugid, Principal principal) {
        User user = ur.findByEmail(principal.getName());
        try {
            if(br.isItTheirBug(Long.parseLong(bugid), user.getId())) {
                Bug bug = br.getBugById(Long.parseLong(bugid));

                if(bug != null) {
                    model.addAttribute("foundBug", bug);

                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", ur.isAdmin(user));
                    model.addAttribute("pageTitle", "Bug #"+bugid+" | Bug Tracker");
                    model.addAttribute("isUnread", nr.isThereUnread(user.getId()));
                    return "bug";
                }
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
        return "error";
    }

    @RequestMapping("/bug/{bugid}/edit")
    public String editBugForm(Model model, @PathVariable(value="bugid") String bugid, Principal principal) {
        User user = ur.findByEmail(principal.getName());
        try {
            if(br.isItTheirBug(Long.parseLong(bugid), user.getId())) {
                Bug bug = br.getBugById(Long.parseLong(bugid));

                if(bug != null) {
                    model.addAttribute("bug", bug);

                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", ur.isAdmin(user));
                    model.addAttribute("pageTitle", "Bug #"+bugid+" | Bug Tracker");
                    model.addAttribute("projects", pr.getProjects(user.getId()));
                    model.addAttribute("isUnread", nr.isThereUnread(user.getId()));
                    return "editBug";
                }
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
        return "error";
    }

    @RequestMapping(value = "/bugs/edit", method = RequestMethod.POST)
    public String editBug(@ModelAttribute Bug bug, Principal principal) {
        User user = ur.findByEmail(principal.getName());
        br.updateBug(bug.getBugId(), pr.getProjectIdByName(bug.getProjectName()), bug.getTitle(), bug.getDescription(), bug.getStatus());

        return "redirect:/bug/"+bug.getBugId();
    }

    @RequestMapping(value = "/bug/delete", method = RequestMethod.POST)
    public String deleteBug(@ModelAttribute Bug bug) {
        Bug b = br.getBugById(bug.getBugId());
        br.deleteBug(b.getBugId());
        return "redirect:/bugs";
    }

}
