package com.xheghun.hng60;

public class ProfilePojo {
    private String firstname;
    private String lastname;

    public ProfilePojo(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public ProfilePojo() {
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}