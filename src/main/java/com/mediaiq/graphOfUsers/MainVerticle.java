package com.mediaiq.graphOfUsers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ranga babu.
 */
public class MainVerticle extends AbstractVerticle {

    private Map<User, Set<Relationship>> graph;

    @Override
    public void start(Future<Void> fut) throws Exception {
        Router router = Router.router(vertx);

        router.route("/").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "text/html")
                        .end("<h1>Welcome to Graph of Users Application</h1>");
            }
        });
        router.post("/user/").handler(this::createUser);
        router.post("/relation/").handler(this::createRelation);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    @Override
    public void stop(Future<Void> fut) throws Exception {

    }

    public void createUser(RoutingContext routingContext) {
        User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
        graph.put(user, new HashSet<>());
    }

    public void createRelation(RoutingContext routingContext) {
        Relationship relationship = Json.decodeValue(routingContext.getBodyAsString(), Relationship.class);
        graph.get(relationship.getUser1()).add(relationship);
    }
}
