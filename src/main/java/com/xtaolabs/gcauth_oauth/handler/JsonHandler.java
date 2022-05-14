package com.xtaolabs.gcauth_oauth.handler;

import java.io.IOException;

import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;


public final class JsonHandler implements HttpContextHandler {
    @Override
    public void handle(Request req, Response res) throws IOException {
        String Login_Html_Url = "https://account.mihoyo.com/gcauth_oauth/login.html";
        res.set("server", "tsa_m");
        res.set("Content-Type", "application/json; charset=utf-8");
        res.set("access-control-allow-credentials", "true");
        res.set("access-control-allow-origin", "https://account.hoyoverse.com");
        res.send(String.format("{\"code\":200,\"data\":{\"auth_url\":\"%s\",\"info\":\"\",\"msg\":\"Success\",\"status\":1}}",
                Login_Html_Url));
    }
}
