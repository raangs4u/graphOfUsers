package com.mediaiq.graphOfUsers;

import com.orientechnologies.orient.jdbc.OrientJdbcConnection;
import com.orientechnologies.orient.jdbc.OrientJdbcDriver;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.Set;

/**
 * @author ranga babu.
 */
public class MainVerticle extends AbstractVerticle {

    private Map<User, Set<Relationship>> graph;

    private UserService userService;

    @Override
    public void start(Future<Void> fut) throws Exception {
        //userService = new UserServiceImpl();

        JsonObject config = new JsonObject()
                .put("url", "jdbc:orientdb:plocal:/tmp/databases/petshop")
                .put("driver_class", "com.orientechnologies.orient.jdbc.OrientJdbcDriver")
                .put("max_pool_size", 30);

        JDBCClient client = JDBCClient.createShared(vertx, config);
        //OrientJdbcConnection orientJdbcConnection = client.getConnection()
        client.getConnection(res -> {
            SQLConnection connection = res.result();
            connection.execute("INSERT into user (id, name, age, sex, location) values (1,'satish', 25, 'male', 'blr'))", handler-> {
                System.out.println("Hello");
                handler.succeeded();
            });
        });
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "text/html")
                .end("<h1>Welcome to Graph of Users Application</h1>"));

        router.post("/user/").handler(this::createUser);

        router.post("/relation/").handler(this::createRelation);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    @Override
    public void stop(Future<Void> fut) throws Exception {
        userService.closeConnections();
    }

    public void createUser(RoutingContext routingContext) {
        User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
        userService.createUser(user);
    }

    public void createRelation(RoutingContext routingContext) {
        Relationship relationship = Json.decodeValue(routingContext.getBodyAsString(), Relationship.class);
        userService.createRelationship(relationship);
    }
}
