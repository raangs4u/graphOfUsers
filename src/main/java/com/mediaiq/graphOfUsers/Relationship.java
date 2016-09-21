package com.mediaiq.graphOfUsers;

/**
 * @author ranga babu
 */
public class Relationship implements Comparable<Relationship> {

    private long id;

    private RelationType relationType;

    private User user1;

    private User user2;

    public Relationship(long id, String relationType, User user1, User user2) {
        this.id = id;
        this.relationType = RelationType.valueOf(relationType);
        this.user1 = user1;
        this.user2 = user2;
    }

    public Relationship(String relationType, User user1, User user2) {
        this.relationType = RelationType.valueOf(relationType);
        this.user1 = user1;
        this.user2 = user2;
    }

    public Relationship() {
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

    public long getId() {
        return id;
    }

    @Override
    public int compareTo(Relationship o) {
        return new Long(this.getId()).compareTo(o.getId());
    }
}
