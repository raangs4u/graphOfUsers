package com.mediaiq.graphOfUsers;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.LinkedList;

/**
 * @author ranga babu.
 */
public class UserServiceImpl implements UserService {

    private ODatabaseDocumentTx db;

    public UserServiceImpl() {
        db = new ODatabaseDocumentTx ("plocal:/tmp/databases/petshop").create();
        //db.open("admin", "admin");
    }

    @Override
    public void createUser(User user) {
        ODocument doc = new ODocument("User");
        doc.field("id", user.getId());
        doc.field("name", user.getName());
        doc.field("age", user.getAge());
        doc.field("sex", user.getSex());
        doc.field("location", user.getLocation());
        db.save(doc);
    }

    @Override
    public void createRelationship(Relationship rel) {
        ODocument doc = new ODocument("Relationship");
        doc.field("id", rel.getId());
        doc.field("user1", rel.getUser1());
        doc.field("user2", rel.getUser2());
        doc.field("relationType", rel.getRelationType());
        db.save(doc);
    }

    @Override
    public LinkedList<User> findShortestPathOfRelationship(User user1, User user2) {
        return null;
    }

    @Override
    public void closeConnections() {
        db.close();
    }
}
