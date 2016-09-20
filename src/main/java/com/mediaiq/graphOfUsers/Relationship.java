package com.mediaiq.graphOfUsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rmandada
 */
@JsonIgnoreProperties(value = {"id"})
public class Relationship {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id;

    private RelationType relationType;

    private User user1;

    private User user2;

    public Relationship(int id, String relationType, User user1, User user2) {
        this.id = id;
        this.relationType = RelationType.valueOf(relationType);
        this.user1 = user1;
        this.user2 = user2;
    }

    public Relationship(String relationType, User user1, User user2) {
        this.relationType = RelationType.valueOf(relationType);
        this.user1 = user1;
        this.user2 = user2;
        this.id = COUNTER.getAndIncrement();
    }

    public Relationship() {
        id = COUNTER.getAndIncrement();
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public int getId() {
        return id;
    }
}
