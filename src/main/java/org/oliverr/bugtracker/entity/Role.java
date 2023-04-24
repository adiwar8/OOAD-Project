package org.oliverr.bugtracker.entity;

import java.util.HashSet;

public class Role {

    private Long id;
    private String role;

    private HashSet<User> users = new HashSet<>();

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Role() {

    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public HashSet<User> getUsers() { return users; }
    public void setUsers(HashSet<User> users) { this.users = users; }
}
