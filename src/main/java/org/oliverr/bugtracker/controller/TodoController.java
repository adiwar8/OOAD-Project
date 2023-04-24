package org.oliverr.bugtracker.controller;

import org.oliverr.bugtracker.entity.Todo;
import org.oliverr.bugtracker.entity.User;
import org.oliverr.bugtracker.repository.TodoRepository;
import org.oliverr.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/addtodo", method = RequestMethod.POST)
    public String handleTodo(@ModelAttribute Todo todo, Principal principal) {
        User sender = userRepository.findByEmail(principal.getName());
        todoRepository.addTodo(todo.getTask(), sender.getId());

        return "redirect:/";
    }

}
