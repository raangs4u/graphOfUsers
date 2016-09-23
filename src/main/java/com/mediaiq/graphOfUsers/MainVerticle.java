package com.mediaiq.graphOfUsers;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;

import java.util.LinkedList;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * @author ranga babu.
 */
public class MainVerticle extends AbstractVerticle {

    private DBVerticle dbVerticle;

    @Override
    public void start(Future<Void> fut) throws Exception {
        dbVerticle = new DBVerticle();
        vertx.deployVerticle(dbVerticle);
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
        router.get("/shortest-path/:userId1/:userId2").handler(this::getShortestPath);

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    @Override
    public void stop(Future<Void> fut) throws Exception {

    }

    public void createUser(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
            User user = Json.decodeValue(buffer.toString(), User.class);
            ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
            dbVerticle.createUser(user);
        });
        routingContext.response()
                .setStatusCode(201)
                .end("New user created successfully.");

    }

    public void createRelation(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
            Relationship relationship = null;
            relationship = Json.decodeValue(buffer.toString(), Relationship.class);
            ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
            dbVerticle.createRelationship(relationship);
        });
        routingContext.response()
                .setStatusCode(201)
                .end("New relation created successfully.");
    }

    public void getShortestPath(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        int userId1 = Integer.parseInt(request.getParam("UserId1"));
        int userId2 = Integer.parseInt(request.getParam("UserId2"));
        ODatabaseRecordThreadLocal.INSTANCE.set(dbVerticle.getDb());
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
