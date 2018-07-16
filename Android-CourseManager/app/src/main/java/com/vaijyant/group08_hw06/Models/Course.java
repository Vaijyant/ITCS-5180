package com.vaijyant.group08_hw06.Models;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by vaijy on 2017-11-03.
 */

public class Course extends RealmObject {
    @PrimaryKey @Required
    private String title;
    private String instructor;
    private String schedDay;
    private String schedTime;
    private byte[] courseImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSchedDay() {
        return schedDay;
    }

    public void setSchedDay(String schedDay) {
        this.schedDay = schedDay;
    }

    public String getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(String schedTime) {
        this.schedTime = schedTime;
    }

    public byte[] getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(byte[] courseImage) {
        this.courseImage = courseImage;
    }
}
