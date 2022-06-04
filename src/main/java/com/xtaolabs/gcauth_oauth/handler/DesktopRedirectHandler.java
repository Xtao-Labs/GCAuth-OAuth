package com.xtaolabs.gcauth_oauth.handler;

import emu.grasscutter.server.http.Router;
import express.Express;
import express.http.Request;
import express.http.Response;

import io.javalin.Javalin;

import static emu.grasscutter.Configuration.*;


public final class DesktopRedirectHandler implements Router {

    @Override
    public void applyRoutes(Express express, Javalin javalin) {
        express.get("/Api/twitter_login", DesktopRedirectHandler::handle);
    }

    public static void handle(Request req, Response res) {
        String Login_Url = ("http" + (HTTP_ENCRYPTION.useEncryption ? "s" : "") + "://"
                + lr(HTTP_INFO.accessAddress, HTTP_INFO.bindAddress) + ":"
                + lr(HTTP_INFO.accessPort, HTTP_INFO.bindPort) + "/gcauth_oauth/login.html");
        res.set("server", "tsa_m");
        res.set("Content-Type", "application/json; charset=utf-8");
        res.set("access-control-allow-credentials", "true");
        res.set("access-control-allow-origin", "https://account.hoyoverse.com");
        res.send(String.format("{\"code\":200,\"data\":{\"auth_url\":\"%s\",\"info\":\"\",\"msg\":\"Success\",\"status\":1}}",
                Login_Url));
    }
}
