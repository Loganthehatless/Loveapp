package com.dostal.loveapp;

import com.google.firebase.firestore.Exclude;

public class User {
    private String name;
    private String role;
    private String id;
    private boolean isactive;
    private boolean isclaimed;

    public User() {
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String name, String role, boolean isactive, boolean isclaimed) {
        this.name = name;
        this.role = role;
        this.isactive = isactive;
        this.isclaimed = isclaimed;
    }

    public boolean isIsclaimed() {
        return isclaimed;
    }

    public void setIsclaimed(boolean isclaimed) {
        this.isclaimed = isclaimed;
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
