package com.vaijyant.group08_hw06.Models;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by vaijy on 2017-11-03.
 */

public class Instructor extends RealmObject {
    @PrimaryKey @Required
    private String email;
    private String firstName;
    private String lastName;
    private String personalWebsite;
    private byte[] instructorImage;

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

    public String getPersonalWebsite() {
        return personalWebsite;
    }

    public void setPersonalWebsite(String personalWebsite) {
        this.personalWebsite = personalWebsite;
    }

    public byte[] getInstructorImage() {
        return instructorImage;
    }

    public void setInstructorImage(byte[] instructorImage) {
        this.instructorImage = instructorImage;
    }


}
