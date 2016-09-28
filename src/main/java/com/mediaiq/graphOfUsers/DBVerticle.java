package com.mediaiq.graphOfUsers;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

/**
 * @author ranga babu
 */
public class DBVerticle extends AbstractVerticle {

    private ODatabaseDocumentTx db;

    @Override
    public void start() throws Exception {
        db = new ODatabaseDocumentTx("plocal:/tmp/databases/userdb").open("admin", "admin");
        try {
            db.getMetadata().getSchema().createClass("User");
            db.getMetadata().getSchema().createClass("Relationships");
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            OSequence userseq = sequenceLibrary.createSequence("userseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
            OSequence relseq = sequenceLibrary.createSequence("relseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
        } catch (Exception e) {

        }
        ODatabaseRecordThreadLocal.INSTANCE.set(db);
    }

    public void createSomeData() {
        ODocument doc = new ODocument("User");
        doc.field("id", getIncrementedUserId());
        doc.field("name", "Ranga");
        doc.field("age", 24);
        doc.field("sex", "male");
        doc.field("location", "Blr");
        db.save(doc);
    }

    @Override
    public void stop() throws Exception {
        db.close();
    }

    /**
     * Creates a user.
     */
    public List<User> createUser(User user) {
        ODocument doc = new ODocument("User");
        doc.field("id", getIncrementedUserId());
        doc.field("name", user.getName());
        doc.field("age", user.getAge());
        doc.field("sex", user.getSex());
        doc.field("location", user.getLocation());
        db.save(doc);
        return getAllUsers();
    }

    /**
     * Creates a relationship.
     */
    public List<Relationship> createRelationship(Relationship rel) {
        ODocument doc = new ODocument("Relationship");
        doc.field("id", getIncrementedRelationId());
        doc.field("user1", Json.encode(rel.getUser1()));
        doc.field("user2", Json.encode(rel.getUser2()));
        doc.field("relationType", rel.getRelationType());
        db.save(doc);
        return getAllRelations();
    }

    /**
     * Obtains all users.
     * @return list of all users.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        ODatabaseDocument database = ODatabaseRecordThreadLocal.INSTANCE.get();
        ORecordIteratorClass<ODocument> users1 = database.browseClass("User");
        for (ODocument document: users1) {
            User user = new User(document.field("id"), document.field("name"), document.field("age"), document.field("sex"), document.field("location"));
            users.add(user);
        }
        Collections.sort(users);
        return users;
    }

    /**
     * Obtains all relations.
     * @return list of all relations.
     */
    public List<Relationship> getAllRelations() {
        List<Relationship> relations = new ArrayList<>();
        ODatabaseDocument database = ODatabaseRecordThreadLocal.INSTANCE.get();
        ORecordIteratorClass<ODocument> relations1 = database.browseClass("Relationship");
        for (ODocument document: relations1) {
            User user1 = Json.decodeValue(document.field("user1"), User.class);
            User user2 = Json.decodeValue(document.field("user2"), User.class);
            Relationship relation = new Relationship(document.field("id"), document.field("relationType"), user1, user2);
            relations.add(relation);
        }
        Collections.sort(relations);
        return relations;
    }

    /**
     * Obtains the shortest path between user1 and user2.
     * @param userId1 id of user1.
     * @param userId2 id of user2.
     * @return path as linked list of users.
     */
    public LinkedList<User> findShortestPathOfRelationship(int userId1, int userId2) {
        LinkedList<User> result = new LinkedList<>();
        Map<Integer, User> userMap = constructUserMap();
        Map<Integer, Set<Relationship>> relationMap = constructRelationMap(userMap);
        List<Vertex> vertexes = shortestPath(userId1, userMap, relationMap);
        getPath(vertexes, vertexes.get(userId1-1), vertexes.get(userId2-1), result, userMap);
        return result;
    }

    private List<Vertex> shortestPath(int userId1, Map<Integer, User> userMap, Map<Integer, Set<Relationship>> relationMap) {
        PriorityQueue<Vertex> queue = new PriorityQueue<>(userMap.size());
        List<Vertex> vertexes = new ArrayList<>();
        for (int i = 0; i < userMap.size(); i++) {
            vertexes.add(null);
        }
        for (Integer user: userMap.keySet()) {
            vertexes.set(user-1, new Vertex(user, Double.POSITIVE_INFINITY, -1));
        }

        vertexes.get(userId1-1).key = 0.0;
        queue.addAll(vertexes);

        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            for (Relationship e : relationMap.get(v.user)){
                Vertex u = vertexes.get((int) e.getUser2().getId()-1);
                if (u.key > v.key + RelationType.getIntValue(e.getRelationType())) {
                    u.key = v.key + RelationType.getIntValue(e.getRelationType());
                    u.pi = v.user;
                }
            }
        }

        return vertexes;

    }

    private void getPath(List<Vertex> vertexes, Vertex u, Vertex v, LinkedList<User> path, Map<Integer, User> users) {
        if (u.user == v.user) {
            path.addLast(users.get(u.user));
        } else if (v.pi == -1) {
            System.out.println("No path from "+ u.user+ " to " + v.user+ " exists");
        } else {
            getPath(vertexes, u, vertexes.get(v.pi-1), path, users);
            path.addLast(users.get(v.user));
        }
    }

    private class Vertex implements Comparable<Vertex>{
        int user;
        Double key ;
        int pi;

        public Vertex(int user, Double key, int pi) {
            this.user = user;
            this.key = key;
            this.pi = pi;
        }

        @Override
        public int compareTo(Vertex o) {
            return key.compareTo(o.key);
        }

        @Override
        public String toString() {
            return String.valueOf(user);
        }
    }

    /**
     * Constructs a map of users with ids.
     */
    public Map<Integer, User> constructUserMap() {
        List<User> users = getAllUsers();
        Map<Integer, User> map = new HashMap<>();
        for (User user: users) {
            map.put((int)user.getId(), user);
        }
        return map;
    }

    /**
     * Constructs a map of user and his relations.
     */
    public Map<Integer, Set<Relationship>> constructRelationMap(Map<Integer, User> userMap) {
        List<Relationship> relationships = getAllRelations();
        Map<Integer, Set<Relationship>> map = new HashMap<>();
        for (Integer userId: userMap.keySet()) {
            map.put(userId, new HashSet<>());
        }
        for (Relationship rel: relationships) {
            int id = (int)rel.getUser1().getId();
            if (!map.containsKey(id)) {
                map.put((int)rel.getUser1().getId(), new HashSet<>());
            }
            map.get(id).add(rel);
        }
        return map;
    }

    /**
     * Generates a auto incremented id for the new user.
     * @return id
     */
    private long getIncrementedUserId() {
        OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("userseq");
        if (seq == null) {
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            seq = sequenceLibrary.createSequence("userseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
        }

        return seq.next();
    }

    /**
     * Generates a auto incremented id for the new relation.
     * @return id
     */
    private long getIncrementedRelationId() {
        OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("relseq");
        if (seq == null) {
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            seq = sequenceLibrary.createSequence("relseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
        }

        return seq.next();
    }

    public ODatabaseDocumentTx getDb() {
        return db;
    }

}
