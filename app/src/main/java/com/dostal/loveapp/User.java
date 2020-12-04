package com.dostal.loveapp;

public class User {
    private String name;
    private String role;
    private boolean isactive;

    public User() {
    }

    public User(String name, String role, boolean isactive) {
        this.name = name;
        this.role = role;
        this.isactive = isactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
}
