package com.mediaiq.graphOfUsers;

import java.util.LinkedList;

/**
 * @author ranga babu.
 */
public interface UserService {
    void createUser(User user);

    void createRelationship(Relationship rel);

    LinkedList<User> findShortestPathOfRelationship(User user1, User user2);

    void closeConnections();
}
