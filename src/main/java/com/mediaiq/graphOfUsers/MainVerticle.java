package com.mediaiq.graphOfUsers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ranga babu.
 */
public class MainVerticle extends AbstractVerticle {

    private Map<User, Set<Relationship>> graph;

    private UserService userService;

    private DBVerticle dbVerticle;

    @Override
    public void start(Future<Void> fut) throws Exception {
        dbVerticle = new DBVerticle();
        vertx.deployVerticle(dbVerticle);
        System.out.println("THReadName: ####: "+ Thread.currentThread().getName());
        //ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb().getUnderlying());
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "text/html")
                .end("<h1>Welcome to Graph of Users Application</h1>"));

        router.post("/user/").handler(this::createUser);
        router.get("/user/:id").handler(this::getUser);
        router.get("/users/").handler(this::getAllUsers);

        router.post("/relation/").handler(this::createRelation);
        router.get("/relations").handler(this::getAllRelations);
        router.get("shortest-path/:userId1/:userId2").handler(this::getShortestPath);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    @Override
    public void stop(Future<Void> fut) throws Exception {
       // userService.closeConnections();
    }

    public void createUser(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
            Gson gson = new Gson();
            JsonElement element = gson.fromJson (buffer.toString(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            User user = new User();
            user.setName(jsonObj.get("name").getAsString());
            user.setAge(jsonObj.get("age").getAsInt());
            user.setSex(jsonObj.get("sex").getAsString());
            user.setSex(jsonObj.get("location").getAsString());
            ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
            dbVerticle.createUser(user);
        });

    }

    public void createRelation(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
            ObjectMapper mapper = new ObjectMapper();
            Relationship relationship = null;
            relationship = Json.decodeValue(buffer.toString(), Relationship.class);
            ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
            dbVerticle.createRelationship(relationship);
        });
    }

    public void getShortestPath(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        int userId1 = Integer.parseInt(request.getParam("UserId1"));
        int userId2 = Integer.parseInt(request.getParam("UserId2"));
        LinkedList<User> users = dbVerticle.findShortestPathOfRelationship(userId1, userId2);

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(users));
    }


    public void getUser(RoutingContext routingContext) {
        ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
        User user = dbVerticle.getAllUsers().get(0);
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(user));
    }

    public void getAllUsers(RoutingContext routingContext) {
        ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
        List<User> users = dbVerticle.getAllUsers();
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(users));
    }

    public void getAllRelations(RoutingContext routingContext) {
        ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
        List<Relationship> users = dbVerticle.getAllRelations();
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(users));
    }

}
