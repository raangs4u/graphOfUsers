package com.mediaiq.graphOfUsers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ranga babu.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {

    /*private static final AtomicInteger COUNTER = new AtomicInteger();*/

    private long id;

    private String name;

    private int age;

    private String sex;

    public User(long id, String name, int age, String sex, String location) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.location = location;
    }

    private String location;

    public User(String name, int age, String sex, String location) {
        /*this.id = COUNTER.getAndIncrement();*/
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.location = location;
    }

    public User() {
        /*this.id = COUNTER.getAndIncrement();*/
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
