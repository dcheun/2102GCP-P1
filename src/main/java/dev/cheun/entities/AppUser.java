package dev.cheun.entities;

public class AppUser {
    private int id;
    private String fname;
    private String lname;
    private String email;
    private int userRole;

    public AppUser() {
    }

    public AppUser(int id, String fname, String lname, String email, int userRole) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.userRole = userRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", user_role=" + userRole +
                '}';
    }
}
