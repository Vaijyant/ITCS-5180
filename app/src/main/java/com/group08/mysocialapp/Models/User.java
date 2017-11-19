package com.group08.mysocialapp.Models;

import java.io.Serializable;

/**
 * Created by vaijy on 2017-11-16.
 */

public class User implements Serializable {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String password;

    public User() {
    }

    public User(String email, String firstName, String lastName, String dateOfBirth, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "\"id\": \"" + id + '\"' +
                ", \"email\": \"" + email + '\"' +
                ", \"firstName\": \"" + firstName + '\"' +
                ", \"lastName\": \"" + lastName + '\"' +
                ", \"dateOfBirth\": \"" + dateOfBirth + '\"' +
                ", \"password\": \"" + password + '\"' +
                '}';
    }
}
