package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UserModel;
import play.Logger;
import play.libs.EventSource;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.twirl.api.Html;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static play.libs.F.Promise;

public class Application extends Controller {

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result main() {
        return ok(views.html.app.render(Html.apply("Some sample content")));
    }

    public Result loginGet() {
        return ok(views.html.login.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Promise<Result> loginPost() {
        return Promise.promise(() -> {
            if (!request().method().equalsIgnoreCase("POST"))
                return forbidden();

            final JsonNode reqNode = request().body().asJson();
            final String usernameQuery = reqNode.get("username").asText();
            final String passwordQuery = reqNode.get("password").asText();

            Logger.error("loginPost; Username:" + usernameQuery + " password:" + passwordQuery);

            List<UserModel> list = UserModel.find.where().eq("email", usernameQuery).findList();
            if (list == null || list.size() != 1)
                return notFound();

            // create session object

            ObjectNode node = new ObjectMapper().createObjectNode()
                    .put("session", "abc123");

            return ok(node);
        });
    }

    public Result syncFoo() {
        return ok("sync foo");
    }

    public Promise<Result> asyncFoo() {
        return Promise.promise(() -> ok("async foo"));
    }

    public Promise<Result> asyncNonBlockingFoo() {
        return Promise.delayed(() -> ok("async non-blocking foo"), 5, TimeUnit.SECONDS);
    }

    public Promise<Result> reactiveRequest() {
        Promise<WSResponse> typesafePromise = WS.url("http://www.typesafe.com").get();
        return typesafePromise.map(response -> ok(response.getBody()));
    }

    public Promise<Result> reactiveComposition() {
        final Promise<WSResponse> twitterPromise = WS.url("http://www.twitter.com").get();
        final Promise<WSResponse> typesafePromise = WS.url("http://www.typesafe.com").get();

        return twitterPromise.flatMap((twitter) ->
                typesafePromise.map((typesafe) ->
                        ok(twitter.getBody() + typesafe.getBody())));
    }

    public Result events() {
        EventSource eventSource = new EventSource() {
            public void onConnected() {
                send(EventSource.Event.event("hello"));
            }
        };
        return ok(eventSource);
    }

    public Promise<Result> listUsers() {
        List<UserModel> users = UserModel.find.all();
        return Promise.pure(ok("Users:" + users.size()));
    }

    public WebSocket<String> echo() {
        return new WebSocket<String>() {
            public void onReady(final In<String> in, final Out<String> out) {
                in.onMessage(out::write);
            }
        };
    }

}
