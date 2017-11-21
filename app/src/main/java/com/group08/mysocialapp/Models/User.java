package com.group08.mysocialapp.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaijy on 2017-11-16.
 */

public class User implements Serializable{

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String password;
    private List<String> friends;
    private List<String> requestTo;
    private List<String> requestFrom;
    @Exclude
    String marker;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.password = user.getPassword();

        this.friends = new ArrayList<>();
        if (user.getFriends() != null)
            this.friends.addAll(user.getFriends());

        this.requestTo = new ArrayList<>();
        if (user.getRequestTo() != null)
            this.requestTo.addAll(requestTo);

        this.requestFrom = new ArrayList<>();
        if (user.getRequestFrom() != null)
            this.requestFrom.addAll(requestFrom);

    }

    public User(String id, String email, String firstName, String lastName, String dateOfBirth,
                String password, String friends, String requestTo,
                String requestFrom) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.friends = new ArrayList<>();
        this.friends.add(friends);
        this.requestTo = new ArrayList<>();
        this.requestTo.add(requestTo);
        this.requestFrom = new ArrayList<>();
        this.requestFrom.add(requestFrom);
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

    public void addFriends(String friendId) {
        if (this.friends == null)
            this.friends = new ArrayList<>();
        this.friends.add(friendId);
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getRequestTo() {
        return requestTo;
    }

    public void addRequestTo(String requestToId) {
        if (this.requestTo == null)
            this.requestTo = new ArrayList<>();
        this.requestTo.add(requestToId);
    }

    public List<String> getRequestFrom() {
        return requestFrom;
    }

    public void addRequestFrom(String requestFromId) {
        if (this.requestFrom == null)
            this.requestFrom = new ArrayList<>();
        this.requestFrom.add(requestFromId);
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
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
                ", \"friends\": \"" + friends + '\"' +
                '}';
    }
}
