package com.androiddeft.firebasepushnotification;

public class User {

    private int id;
    private String name, department, session, username, email, rudf_position;

    public User(int id, String name, String department, String session, String username, String email, String rudf_position) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.session = session;
        this.username = username;
        this.email = email;
        this.rudf_position = rudf_position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRudf_position() {
        return rudf_position;
    }

}