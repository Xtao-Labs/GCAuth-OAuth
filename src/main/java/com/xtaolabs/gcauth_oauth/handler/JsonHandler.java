package com.xtaolabs.gcauth_oauth.handler;

import java.io.IOException;

import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;

import static emu.grasscutter.Configuration.*;
import static emu.grasscutter.Configuration.DISPATCH_INFO;


public final class JsonHandler implements HttpContextHandler {
    @Override
    public void handle(Request req, Response res) throws IOException {
        String Login_Url = ("http" + (DISPATCH_ENCRYPTION.useEncryption ? "s" : "") + "://"
                + lr(DISPATCH_INFO.accessAddress, DISPATCH_INFO.bindAddress) + ":"
                + lr(DISPATCH_INFO.accessPort, DISPATCH_INFO.bindPort) + "/gcauth_oauth/login.html");
        res.set("server", "tsa_m");
        res.set("Content-Type", "application/json; charset=utf-8");
        res.set("access-control-allow-credentials", "true");
        res.set("access-control-allow-origin", "https://account.hoyoverse.com");
        res.send(String.format("{\"code\":200,\"data\":{\"auth_url\":\"%s\",\"info\":\"\",\"msg\":\"Success\",\"status\":1}}",
                Login_Url));
    }
}
