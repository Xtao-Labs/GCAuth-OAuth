package com.xtaolabs.gcauth_oauth.handler;

import emu.grasscutter.server.http.Router;
import static emu.grasscutter.config.Configuration.*;

import express.Express;
import express.http.Request;
import express.http.Response;

import io.javalin.Javalin;


public final class MobileRedirectHandler implements Router {

    @Override
    public void applyRoutes(Express express, Javalin javalin) {
        express.get("/sdkTwitterLogin.html", MobileRedirectHandler::handle);
    }

    public static void handle(Request req, Response res) {
        String Login_Url = ("http" + (HTTP_ENCRYPTION.useEncryption ? "s" : "") + "://"
                + lr(HTTP_INFO.accessAddress, HTTP_INFO.bindAddress) + ":"
                + lr(HTTP_INFO.accessPort, HTTP_INFO.bindPort) + "/gcauth_oauth/login.html");
        res.send(String.format("<meta http-equiv=\"refresh\" content=\"0;url=%s\">", Login_Url));
    }
}
