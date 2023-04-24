package org.oliverr.bugtracker.entity;

import java.util.HashSet;

public class User {

    private Long id;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String created;
    private String lastLogin;
    private String image;
    private boolean isAdmin;

    private HashSet<Role> roles = new HashSet<Role>();

    public User() {

    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public HashSet<Role> getRoles() { return roles; }
    public void setRoles(HashSet<Role> roles) { this.roles = roles; }
    public void addToRoles(Role r) { roles.add(r); }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

}
