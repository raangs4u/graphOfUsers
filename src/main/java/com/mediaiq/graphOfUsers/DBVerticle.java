package com.mediaiq.graphOfUsers;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClassImpl;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceCached;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceOrdered;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

/**
 * @author rmandada
 */
public class DBVerticle extends AbstractVerticle {

    public ODatabaseDocumentTx getDb() {
        return db;
    }

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
            //e.printStackTrace();
        }
        ODatabaseRecordThreadLocal.INSTANCE.set(db);
        //createSomeData();
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

    //@Override
    public void createUser(User user) {
        ODocument doc = new ODocument("User");
        doc.field("id", getIncrementedUserId());
        doc.field("name", user.getName());
        doc.field("age", user.getAge());
        doc.field("sex", user.getSex());
        doc.field("location", user.getLocation());
        db.save(doc);
    }

    //@Override
    public void createRelationship(Relationship rel) {
        ODocument doc = new ODocument("Relationship");
        doc.field("id", getIncrementedRelationId());
        doc.field("user1", Json.encode(rel.getUser1()));
        doc.field("user2", Json.encode(rel.getUser2()));
        doc.field("relationType", rel.getRelationType());
        db.save(doc);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        ODatabaseDocument database = ODatabaseRecordThreadLocal.INSTANCE.get();
        ORecordIteratorClass<ODocument> users1 = database.browseClass("User");
        for (ODocument document: users1) {
            User user = new User(document.field("id"), document.field("name"), document.field("age"), document.field("sex"), document.field("location"));
            users.add(user);
        }
        return users;
    }

    public List<Relationship> getAllRelations() {
        List<Relationship> relations = new ArrayList<>();
        ODatabaseDocument database = ODatabaseRecordThreadLocal.INSTANCE.get();
        ORecordIteratorClass<ODocument> users1 = database.browseClass("Relationship");
        for (ODocument document: users1) {
            User user1 = Json.decodeValue(document.field("user1"), User.class);
            User user2 = Json.decodeValue(document.field("user2"), User.class);
            Relationship relation = new Relationship(document.field("id"), document.field("relationType"), user1, user2);
            relations.add(relation);
        }
        return relations;
    }

    public LinkedList<User> findShortestPathOfRelationship(int userId1, int userId2) {
        LinkedList<User> result = new LinkedList<>();
        Map<Integer, User> userMap = constructUserMap();
        Map<Integer, Set<Relationship>> relationMap = constructRelationMap(userMap);
        List<Vertex> vertexes = shortestPath(userId1, userMap, relationMap);
        getPath(vertexes, vertexes.get(userId1-1), vertexes.get(userId2-1), result, userMap);
        //Collections.reverse(result);
        return result;
    }

    public List<Vertex> shortestPath(int userId1, Map<Integer, User> userMap, Map<Integer, Set<Relationship>> relationMap) {
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

    public static void getPath(List<Vertex> vertexes, Vertex u, Vertex v, LinkedList<User> path, Map<Integer, User> users) {
        if (u.user == v.user) {
            path.addLast(users.get(u.user));
        } else if (v.pi == -1) {
            System.out.println("No path from "+ u.user+ " to " + v.user+ " exists");
        } else {
            getPath(vertexes, u, vertexes.get(v.pi-1), path, users);
            path.addLast(users.get(v.user));
        }
    }

    static class Vertex implements Comparable<Vertex>{
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

    public Map<Integer, User> constructUserMap() {
        List<User> users = getAllUsers();
        Map<Integer, User> map = new HashMap<>();
        for (User user: users) {
            map.put((int)user.getId(), user);
        }
        return map;
    }

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

    private long getIncrementedUserId() {
        OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("userseq");
        if (seq == null) {
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            seq = sequenceLibrary.createSequence("userseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
        }

        return seq.next();
    }

    private long getIncrementedRelationId() {
        OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("relseq");
        if (seq == null) {
            OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
            seq = sequenceLibrary.createSequence("relseq", OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(0l).setIncrement(1));
        }

        return seq.next();
    }

}
