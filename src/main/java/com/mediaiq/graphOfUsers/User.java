package com.mediaiq.graphOfUsers;

/**
 * @author ranga babu.
 */
public class User implements Comparable<User> {

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
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.location = location;
    }

    public User() {
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

    @Override
    public int compareTo(User o) {
        return new Long(this.getId()).compareTo(o.id);
    }
}
