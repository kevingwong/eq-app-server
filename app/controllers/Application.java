package controllers;

import models.UserModel;
import play.libs.EventSource;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.twirl.api.Html;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static play.libs.F.Promise;

public class Application extends Controller {

    public Result index() {
        return ok(views.html.index.render("Enduraquest"));
    }

    public Result main() {
        return ok(views.html.app.render("Enduraquest", Html.apply("Some sample content")));
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
