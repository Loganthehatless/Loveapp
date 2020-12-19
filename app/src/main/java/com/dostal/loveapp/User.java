package com.dostal.loveapp;

import com.google.firebase.firestore.Exclude;

public class User {
    private String name;
    private String role;
    private String id;
    private boolean isonetime;
    private boolean isclaimed;

    public User() {
    }


    public User(String name, String role, String id, boolean isonetime, boolean isclaimed) {
        this.name = name;
        this.role = role;
        this.id = id;
        this.isonetime = isonetime;
        this.isclaimed = isclaimed;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public User(String name, String id, boolean isonetime) {
        this.name = name;
        this.id = id;
        this.isonetime = isonetime;
    }

    public User(String name, String role, boolean isonetime, boolean isclaimed) {
        this.name = name;
        this.role = role;
        this.isonetime = isonetime;
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

    public boolean isonetime() {
        return isonetime;
    }

    public void isonetime(boolean isonetime) {
        this.isonetime = isonetime;
    }
}
